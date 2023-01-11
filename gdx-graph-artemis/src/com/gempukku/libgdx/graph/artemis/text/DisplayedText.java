package com.gempukku.libgdx.graph.artemis.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.Vector3ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.VectorUtil;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteBatchSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.text.layout.GlyphOffsetLine;
import com.gempukku.libgdx.graph.artemis.text.layout.GlyphOffsetText;
import com.gempukku.libgdx.graph.artemis.text.layout.GlyphOffseter;
import com.gempukku.libgdx.graph.artemis.text.parser.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.DefaultRenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;
import com.gempukku.libgdx.lib.artemis.font.BitmapFontSystem;

public class DisplayedText implements Disposable {
    private static Matrix4 tempMatrix = new Matrix4();
    private static Vector3 tempVector1 = new Vector3();
    private static Vector3 tempVector2 = new Vector3();
    private static Vector3 tempVector3 = new Vector3();
    private static DefaultRenderableSprite tempRenderableSprite = new DefaultRenderableSprite();
    private static HierarchicalRenderableSprite tempHierarchicalRenderableSprite = new HierarchicalRenderableSprite();

    private GlyphOffseter glyphOffseter;
    private CharacterTextParser textParser;
    private SpriteBatchModel spriteBatchModel;
    private BitmapFontSystem bitmapFontSystem;
    private Matrix4 transform;
    private TextBlock textBlock;
    private SpriteBatchSystem spriteBatchSystem;

    private IntArray spriteIdentifiers = new IntArray();
    private ObjectSet<BatchNameWithSpriteIndex> externalSprites = new ObjectSet<>();

    /*
     * Up-Vector (vec3) - vector defining both height and up direction
     * Right-Vector (vec3) - vector defining both width and right direction
     * Font-Texture (texture-region) - font texture region
     * U-Range - range of U in UV of given sprite
     * V-Range - range of V in UV of given sprite
     * Width - character width
     * Edge - character smoothing
     * Color - character color
     */

    public DisplayedText(GlyphOffseter glyphOffseter, CharacterTextParser textParser, SpriteBatchModel spriteBatchModel,
                         BitmapFontSystem bitmapFontSystem, SpriteBatchSystem spriteBatchSystem,
                         Matrix4 transform, TextBlock textBlock) {
        this.glyphOffseter = glyphOffseter;
        this.textParser = textParser;
        this.spriteBatchModel = spriteBatchModel;
        this.bitmapFontSystem = bitmapFontSystem;
        this.spriteBatchSystem = spriteBatchSystem;
        this.transform = transform;
        this.textBlock = textBlock;
        addText();
    }

    public void updateSprites() {
        removeText();
        addText();
    }

    private void addText() {
        TextStyle defaultTextStyle = createDefaultTextStyle();

        CharacterParsedText parsedText = textParser.parseText(defaultTextStyle, textBlock.getText());
        if (parsedText.getNextUnbreakableChunkLength(0) == -1)
            return;

        float widthInWorld = textBlock.getRightVector().len();
        float heightInWorld = textBlock.getUpVector().len();

        try {
            float targetWidth = textBlock.getTargetWidth();
            boolean wrap = textBlock.isWrap();

            GlyphOffsetText offsetText;
            float scale;
            if (textBlock.isScaleDownToFit()) {
                offsetText = layoutTextToFit(widthInWorld, heightInWorld, glyphOffseter, parsedText, targetWidth,
                        textBlock.getScaleDownMultiplier(), textBlock.isWrap());
                scale = Math.min(widthInWorld / targetWidth, calculateScale(offsetText, widthInWorld, heightInWorld));
            } else {
                offsetText = glyphOffseter.offsetText(parsedText, targetWidth, wrap);
                scale = widthInWorld / targetWidth;
            }

            Vector3 unitRightVector = tempVector1.set(textBlock.getRightVector()).nor().scl(scale);
            Vector3 unitUpVector = tempVector2.set(textBlock.getUpVector()).nor().scl(scale);

            float widthInGlyph = widthInWorld / scale;
            float heightInGlyph = heightInWorld / scale;

            ObjectMap<TextStyle, PropertyContainer> stylePropertyContainerMap = new ObjectMap<>();

            TextVerticalAlignment alignment = getVerticalAlignment(offsetText.getTextStyle());

            Matrix4 resultTransform = tempMatrix.set(transform).mul(textBlock.getTransform());

            final float startY = alignment.apply(offsetText.getTextHeight(), heightInGlyph) + heightInGlyph / 2;

            float lineY = 0;
            for (int lineIndex = 0; lineIndex < offsetText.getLineCount(); lineIndex++) {
                GlyphOffsetLine line = offsetText.getLine(lineIndex);
                float lineHeight = line.getHeight();

                TextStyle lineStyle = offsetText.getLineStyle(lineIndex);
                TextHorizontalAlignment horizontalAlignment = getHorizontalAlignment(lineStyle);
                final float startX = horizontalAlignment.apply(line.getWidth(), widthInGlyph) - widthInGlyph / 2;
                for (int glyphIndex = 0; glyphIndex < line.getGlyphCount(); glyphIndex++) {
                    char character = parsedText.getCharAt(line.getStartIndex() + glyphIndex);
                    TextStyle textStyle = line.getGlyphStyle(glyphIndex);
                    BitmapFont bitmapFont = (BitmapFont) textStyle.getAttribute(TextStyleConstants.Font);

                    float fontScale = getFontScale(textStyle);

                    float charX = line.getGlyphXAdvance(glyphIndex);
                    float charY = line.getGlyphYAdvance(glyphIndex) - lineY;

                    TextureRegion textureRegion = getTextureRegion(textStyle);
                    if (textureRegion != null) {
                        tempRenderableSprite.clear();
                        String spriteBatchName = getSpriteSystemName(textStyle);
                        tempRenderableSprite.setValue("UV", SpriteSystem.uvAttribute);
                        float textureHeight = FontUtil.getFontAscent(bitmapFont);
                        float textureWidth = textureHeight * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();
                        Vector3ValuePerVertex positionFloatArray = VectorUtil.createSideSpritePosition(
                                startX + charX, startY + charY,
                                textureWidth, textureHeight,
                                unitRightVector, unitUpVector,
                                resultTransform);
                        tempRenderableSprite.setValue("Position", positionFloatArray);
                        tempRenderableSprite.setValue("Texture", textureRegion);

                        int spriteIndex = spriteBatchSystem.getSpriteBatchModel(spriteBatchName).addSprite(tempRenderableSprite);
                        externalSprites.add(new BatchNameWithSpriteIndex(spriteBatchName, spriteIndex));
                    } else {
                        BitmapFont.Glyph glyph = bitmapFont.getData().getGlyph(character);
                        PropertyContainer stylePropertyContainer = obtainStylePropertyContainer(stylePropertyContainerMap, textStyle);

                        addGlyph(glyph, bitmapFont,
                                resultTransform,
                                unitRightVector, unitUpVector, stylePropertyContainer,
                                fontScale,
                                startX, startY,
                                charX, charY);
                    }
                }
                lineY += lineHeight;
            }
            offsetText.dispose();
        } finally {
            parsedText.dispose();
            Pools.free(defaultTextStyle);
        }
    }

    private PropertyContainer obtainStylePropertyContainer(ObjectMap<TextStyle, PropertyContainer> stylePropertyContainerMap, TextStyle textStyle) {
        PropertyContainer result = stylePropertyContainerMap.get(textStyle);
        if (result == null) {
            MapWritablePropertyContainer container = new MapWritablePropertyContainer();
            container.setValue("Width", getFontWidth(textStyle));
            container.setValue("Edge", getFontEdge(textStyle));
            container.setValue("Color", getFontColor(textStyle));
            result = container;

            stylePropertyContainerMap.put(textStyle, result);
        }
        return result;
    }

    private float getFontScale(TextStyle textStyle) {
        Float fontScale = (Float) textStyle.getAttribute(TextStyleConstants.GlyphScale);
        return fontScale != null ? fontScale : 1f;
    }

    private float getFontWidth(TextStyle textStyle) {
        Float fontWidth = (Float) textStyle.getAttribute(TextStyleConstants.FontWidth);
        return fontWidth != null ? fontWidth : textBlock.getWidth();
    }

    private float getFontEdge(TextStyle textStyle) {
        Float fontEdge = (Float) textStyle.getAttribute(TextStyleConstants.FontEdge);
        return fontEdge != null ? fontEdge : textBlock.getEdge();
    }

    private Color getFontColor(TextStyle textStyle) {
        Color fontColor = (Color) textStyle.getAttribute(TextStyleConstants.FontColor);
        return fontColor != null ? fontColor : textBlock.getColor();
    }

    private TextVerticalAlignment getVerticalAlignment(TextStyle textStyle) {
        TextVerticalAlignment alignment = (TextVerticalAlignment) textStyle.getAttribute(TextStyleConstants.AlignmentVertical);
        return alignment != null ? alignment : textBlock.getVerticalAlignment();
    }

    private TextHorizontalAlignment getHorizontalAlignment(TextStyle textStyle) {
        TextHorizontalAlignment alignment = (TextHorizontalAlignment) textStyle.getAttribute(TextStyleConstants.AlignmentHorizontal);
        return alignment != null ? alignment : textBlock.getHorizontalAlignment();
    }

    private TextureRegion getTextureRegion(TextStyle textStyle) {
        return (TextureRegion) textStyle.getAttribute(TextStyleConstants.ImageTextureRegion);
    }

    private String getSpriteSystemName(TextStyle textStyle) {
        return (String) textStyle.getAttribute(TextStyleConstants.ImageSpriteSystemName);
    }

    private TextStyle createDefaultTextStyle() {
        TextStyle textStyle = Pools.obtain(TextStyle.class);
        textStyle.setAttribute(TextStyleConstants.Kerning, textBlock.getKerning());
        textStyle.setAttribute(TextStyleConstants.Font, bitmapFontSystem.getBitmapFont(textBlock.getBitmapFontPath()));
        textStyle.setAttribute(TextStyleConstants.GlyphSpacing, textBlock.getLetterSpacing());
        textStyle.setAttribute(TextStyleConstants.LineSpacing, textBlock.getLineSpacing());
        textStyle.setAttribute(TextStyleConstants.ParagraphSpacing, textBlock.getParagraphSpacing());
        textStyle.setAttribute(TextStyleConstants.AlignmentHorizontal, textBlock.getHorizontalAlignment());
        return textStyle;
    }

    private GlyphOffsetText layoutTextToFit(float width, float height, GlyphOffseter glyphOffseter,
                                            ParsedText text, float targetWidth, float scaleDownMultiplier, boolean wrap) {
        float scale = width / targetWidth;
        float renderScale = 1f;
        do {
            GlyphOffsetText offsetText = glyphOffseter.offsetText(text, targetWidth * renderScale, wrap);
            if (offsetText.getTextWidth() * scale <= width && offsetText.getTextHeight() * scale <= height)
                return offsetText;

            scale /= scaleDownMultiplier;
            renderScale *= scaleDownMultiplier;
            offsetText.dispose();
        } while (true);
    }

    private float calculateScale(GlyphOffsetText offsetText, float width, float height) {
        return Math.min(width / offsetText.getTextWidth(), height / offsetText.getTextHeight());
    }

    private void addGlyph(BitmapFont.Glyph glyph, BitmapFont bitmapFont,
                          Matrix4 resultTransform,
                          Vector3 unitRightVector, Vector3 unitUpVector,
                          PropertyContainer basePropertyContainer,
                          float glyphScale,
                          float startX, float startY,
                          float glyphX, float glyphY) {
        float glyphXOffset = glyph.xoffset * glyphScale;
        float glyphYOffset = glyph.yoffset * glyphScale;

        float glyphWidth = glyph.width * glyphScale;
        float glyphHeight = glyph.height * glyphScale;

        Vector3ValuePerVertex positionFloatArray = VectorUtil.createSideSpritePosition(
                startX + glyphX + glyphXOffset, startY + glyphY - (glyphHeight + glyphYOffset),
                glyphWidth, glyphHeight,
                unitRightVector, unitUpVector,
                resultTransform);

        TextureRegion fontTexture = bitmapFont.getRegion(glyph.page);

        tempHierarchicalRenderableSprite.clear();
        tempHierarchicalRenderableSprite.setParent(basePropertyContainer);
        tempHierarchicalRenderableSprite.setValue("Position", positionFloatArray);
        tempHierarchicalRenderableSprite.setValue("UV", createUVFloatArray(glyph));
        tempHierarchicalRenderableSprite.setValue("Font-Texture", fontTexture);

        spriteIdentifiers.add(spriteBatchModel.addSprite(tempHierarchicalRenderableSprite));
    }

    private Vector2ValuePerVertex createUVFloatArray(BitmapFont.Glyph glyph) {
        return new Vector2ValuePerVertex(new float[]{
                glyph.u, glyph.v2, glyph.u2, glyph.v2, glyph.u, glyph.v, glyph.u2, glyph.v});
    }

    private void removeText() {
        for (int i = 0; i < spriteIdentifiers.size; i++) {
            int spriteIdentifier = spriteIdentifiers.get(i);
            spriteBatchModel.removeSprite(spriteIdentifier);
        }
        spriteIdentifiers.clear();
        for (BatchNameWithSpriteIndex externalSprite : externalSprites) {
            spriteBatchSystem.getSpriteBatchModel(externalSprite.batchName).removeSprite(externalSprite.spriteIndex);
        }
        externalSprites.clear();
    }

    @Override
    public void dispose() {
        removeText();
    }

    public static class BatchNameWithSpriteIndex {
        private String batchName;
        private int spriteIndex;

        public BatchNameWithSpriteIndex(String batchName, int spriteIndex) {
            this.batchName = batchName;
            this.spriteIndex = spriteIndex;
        }
    }
}
