package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.sprite.SpriteProducer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class RenderingSystem extends EntitySystem implements SpriteProducer.TextureLoader, Disposable, EntityListener {
    private final TimeProvider timeProvider;
    private final PipelineRenderer pipelineRenderer;
    private final SpriteProducer.TextureLoader textureLoader;
    private ImmutableArray<Entity> spriteEntities;

    public RenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer,
                           SpriteProducer.TextureLoader textureLoader) {
        super(priority);
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
        this.textureLoader = textureLoader;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family sprite = Family.all(SpriteComponent.class).get();
        spriteEntities = engine.getEntitiesFor(sprite);
        engine.addEntityListener(sprite, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

        Sprite sprite = SpriteProducer.createSprite(entity, this, spriteComponent);

        ObjectMap<String, GraphSprite> spriteObjectMap = new ObjectMap<>();
        spriteComponent.setGraphSprites(spriteObjectMap);
        spriteComponent.setSprite(sprite);

        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
        for (String tag : spriteComponent.getTags()) {
            GraphSprite graphSprite = graphSprites.addSprite(tag, sprite);
            spriteObjectMap.put(tag, graphSprite);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void update(float delta) {
        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
        for (Entity spriteEntity : spriteEntities) {
            SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
            boolean wasDirty = sprite.getSprite().cleanup(timeProvider, pipelineRenderer);
            if (wasDirty) {
                for (GraphSprite graphSprite : sprite.getGraphSprites().values()) {
                    graphSprites.updateSprite(graphSprite);
                }
            }
        }
    }


    @Override
    public Texture loadTexture(String path) {
        return textureLoader.loadTexture(path);
    }

    @Override
    public void dispose() {
    }
}
