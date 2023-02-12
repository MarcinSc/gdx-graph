package com.gempukku.libgdx.graph.artemis.text;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteBatchSystem;
import com.gempukku.libgdx.graph.artemis.text.layout.DefaultGlyphOffseter;
import com.gempukku.libgdx.graph.artemis.text.layout.GlyphOffseter;
import com.gempukku.libgdx.graph.artemis.text.parser.CharacterTextParser;
import com.gempukku.libgdx.graph.artemis.text.parser.DefaultTextParser;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.storage.MultiPartBatchModel;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.font.BitmapFontSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

public class TextSystem extends BaseEntitySystem {
    private BitmapFontSystem bitmapFontSystem;
    private SpriteBatchSystem spriteBatchSystem;
    private TransformSystem transformSystem;
    private PipelineRendererSystem pipelineRendererSystem;
    private ComponentMapper<TextComponent> textComponentMapper;

    private final IntMap<DisplayedText> renderedTexts = new IntMap<>();

    private final GlyphOffseter glyphOffseter = new DefaultGlyphOffseter();
    private CharacterTextParser defaultTextParser;
    private ObjectMap<String, CharacterTextParser> textParserMap = new ObjectMap<>();
    private String defaultSpriteBatchName;

    private Array<Entity> insertedEntities = new Array<>();
    private Array<Entity> removedEntities = new Array<>();

    public TextSystem() {
        this(new DefaultTextParser());
    }

    public TextSystem(CharacterTextParser defaultTextParser) {
        super(Aspect.all(TextComponent.class));
        this.defaultTextParser = defaultTextParser;
    }

    public void setDefaultSpriteBatchName(String defaultSpriteBatchName) {
        this.defaultSpriteBatchName = defaultSpriteBatchName;
    }

    public void setDefaultTextParser(CharacterTextParser textParser) {
        this.defaultTextParser = textParser;
    }

    public void setTextParser(String name, CharacterTextParser characterTextParser) {
        textParserMap.put(name, characterTextParser);
    }

    @Override
    protected void inserted(int entityId) {
        insertedEntities.add(world.getEntity(entityId));
    }

    @Override
    protected void removed(int entityId) {
        removedEntities.add(world.getEntity(entityId));
    }

    private void addSprites(Entity textEntity) {
        Matrix4 resolvedTransform = transformSystem.getResolvedTransform(textEntity);

        TextComponent textBlock = textComponentMapper.get(textEntity);
        String spriteBatchName = determineSpriteBatchName(textBlock);
        CharacterTextParser textParser = determineTextParser(textBlock);

        MultiPartBatchModel<RenderableSprite, SpriteReference> multiPartBatchModel = spriteBatchSystem.getSpriteBatchModel(spriteBatchName);

        DisplayedText text = new DisplayedText(glyphOffseter, textParser, multiPartBatchModel, bitmapFontSystem,
                spriteBatchSystem, resolvedTransform, textBlock);

        renderedTexts.put(textEntity.getId(), text);
    }

    private String determineSpriteBatchName(TextComponent textBlock) {
        String spriteBatchName = textBlock.getSpriteBatchName();
        if (spriteBatchName == null)
            spriteBatchName = defaultSpriteBatchName;
        return spriteBatchName;
    }

    private CharacterTextParser determineTextParser(TextComponent textBlock) {
        CharacterTextParser textParser;
        String textParserName = textBlock.getTextParser();
        if (textParserName != null)
            textParser = textParserMap.get(textParserName, defaultTextParser);
        else
            textParser = defaultTextParser;
        return textParser;
    }

    private void removeSprites(Entity textEntity) {
        DisplayedText text = renderedTexts.remove(textEntity.getId());
        text.dispose();
    }

    @EventListener
    public void transformChanged(TransformUpdated transformUpdated, Entity entity) {
        DisplayedText text = renderedTexts.get(entity.getId());
        if (text != null) {
            text.updateSprites();
        }
    }

    public void updateText(int entityId) {
        Entity textEntity = world.getEntity(entityId);
        removeSprites(textEntity);
        addSprites(textEntity);
    }

    @Override
    protected void processSystem() {
        for (Entity insertedEntity : insertedEntities) {
            addSprites(insertedEntity);
        }
        for (Entity removedEntity : removedEntities) {
            removeSprites(removedEntity);
        }
        insertedEntities.clear();
        removedEntities.clear();
    }

    @Override
    public void dispose() {
        renderedTexts.clear();
    }
}
