package com.gempukku.libgdx.graph.artemis.text.parser;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.artemis.text.FontUtil;

public class DefaultTextParser implements CharacterTextParser {

    @Override
    public CharacterParsedText parseText(TextStyle defaultTextStyle, String text) {
        DefaultParsedText result = Pools.obtain(DefaultParsedText.class);
        result.setTextStyle(defaultTextStyle);
        result.setText(text);
        return result;
    }

    public static class DefaultParsedText implements CharacterParsedText, Pool.Poolable {
        private boolean defaultKerning = true;
        private TextStyle textStyle;
        private String text;

        public void setTextStyle(TextStyle textStyle) {
            this.textStyle = textStyle;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public int getNextUnbreakableChunkLength(int startIndex) {
            if (startIndex >= text.length())
                return -1;
            int textLength = text.length();
            for (int i = startIndex; i < textLength; i++) {
                char c = text.charAt(i);
                if (Character.isWhitespace(c))
                    return 1 + i - startIndex;
            }
            return textLength - startIndex;
        }

        @Override
        public TextStyle getTextStyle(int glyphIndex) {
            return textStyle;
        }

        @Override
        public float getKerning(int glyphIndex) {
            TextStyle style = getTextStyle(glyphIndex);
            if (!getKerning(style))
                return 0f;
            TextStyle lastStyle = getTextStyle(glyphIndex - 1);
            if (lastStyle != style)
                return 0f;

            BitmapFont font = getFont(style);
            BitmapFont.BitmapFontData fontData = font.getData();
            char lastChar = getCharAt(glyphIndex - 1);
            char currentChar = getCharAt(glyphIndex);
            return fontData.getGlyph(lastChar).getKerning(currentChar);
        }

        @Override
        public float getDescent(TextStyle style) {
            BitmapFont font = getFont(style);
            return FontUtil.getFontDescent(font);
        }

        @Override
        public float getAscent(TextStyle style) {
            BitmapFont font = getFont(style);
            return FontUtil.getFontAscent(font);
        }

        @Override
        public float getWidth(int glyphIndex) {
            BitmapFont.BitmapFontData fontData = getFont(getTextStyle(glyphIndex)).getData();
            BitmapFont.Glyph glyph = fontData.getGlyph(getCharAt(glyphIndex));
            return glyph.xadvance;
        }

        @Override
        public char getCharAt(int glyphIndex) {
            return text.charAt(glyphIndex);
        }

        @Override
        public boolean isWhitespace(int glyphIndex) {
            return Character.isWhitespace(getCharAt(glyphIndex));
        }

        @Override
        public boolean isLineBreak(int glyphIndex) {
            return getCharAt(glyphIndex) == '\n';
        }

        private Boolean getKerning(TextStyle textStyle) {
            Boolean kerning = (Boolean) textStyle.getAttribute(TextStyleConstants.Kerning);
            return kerning != null ? kerning : defaultKerning;
        }

        private BitmapFont getFont(TextStyle textStyle) {
            return (BitmapFont) textStyle.getAttribute(TextStyleConstants.Font);
        }

        @Override
        public void reset() {
            textStyle = null;
            text = null;
        }

        @Override
        public void dispose() {
            Pools.free(this);
        }
    }
}
