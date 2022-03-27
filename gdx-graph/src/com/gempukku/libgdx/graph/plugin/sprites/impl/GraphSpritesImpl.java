package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphSpritesImpl implements GraphSprites, RuntimePipelinePlugin, Disposable {
    private final int spriteBatchSize;

    private final ObjectMap<String, BatchedTagSpriteData> batchedTagSpriteData = new ObjectMap<>();
    private final ObjectMap<String, NonBatchedTagSpriteData> nonBatchedTagSpriteData = new ObjectMap<>();

    private final ObjectMap<String, ObjectSet<GraphSprite>> nonBatchedSpritesByTag = new ObjectMap<>();

    private final ObjectMap<String, MapWritablePropertyContainer> tagPropertyContainers = new ObjectMap<>();

    public GraphSpritesImpl(int spriteBatchSize) {
        this.spriteBatchSize = spriteBatchSize;
    }

    @Override
    public GraphSprite addSprite(String tag, RenderableSprite renderableSprite) {
        GraphSpriteImpl graphSprite = new GraphSpriteImpl(tag, renderableSprite);
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null) {
            batchedTagSpriteData.addSprite(graphSprite);
        } else {
            nonBatchedSpritesByTag.get(tag).add(graphSprite);
        }
        return graphSprite;
    }

    @Override
    public void updateSprite(GraphSprite sprite) {
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(sprite.getTag());
        if (batchedTagSpriteData != null)
            batchedTagSpriteData.spriteUpdated(sprite);
    }

    @Override
    public void removeSprite(GraphSprite sprite) {
        String tag = sprite.getTag();
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null)
            batchedTagSpriteData.removeSprite(sprite);
        else
            nonBatchedSpritesByTag.get(tag).remove(sprite);
    }

    @Override
    public void setGlobalProperty(String tag, String name, Object value) {
        MapWritablePropertyContainer propertyContainer = tagPropertyContainers.get(tag);
        propertyContainer.setValue(name, value);
    }

    @Override
    public void unsetGlobalProperty(String tag, String name) {
        MapWritablePropertyContainer propertyContainer = tagPropertyContainers.get(tag);
        propertyContainer.remove(name);
    }

    @Override
    public Object getGlobalProperty(String tag, String name) {
        MapWritablePropertyContainer propertyContainer = tagPropertyContainers.get(tag);
        return propertyContainer.getValue(name);
    }

    public PropertyContainer getGlobalProperties(String tag) {
        return tagPropertyContainers.get(tag);
    }

    public boolean hasSpriteWithTag(String tag) {
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null && batchedTagSpriteData.hasSprites())
            return true;

        return !nonBatchedSpritesByTag.get(tag).isEmpty();
    }

    public Iterable<? extends Array<BatchedSpriteData>> getBatchedSpriteData(String tag) {
        return batchedTagSpriteData.get(tag).getSpriteData();
    }

    public NonBatchedTagSpriteData getNonBatchedSpriteData(String tag) {
        return nonBatchedTagSpriteData.get(tag);
    }

    public Iterable<GraphSprite> getNonBatchedSprites(String tag) {
        return nonBatchedSpritesByTag.get(tag);
    }

    public void registerTag(String tag, SpriteGraphShader shader, boolean batched) {
        if (tagPropertyContainers.containsKey(tag))
            throw new IllegalStateException("There is already a shader with tag: " + tag);

        VertexAttributes vertexAttributes = shader.getVertexAttributes();
        ObjectMap<String, PropertySource> shaderProperties = shader.getProperties();
        Array<String> textureUniformNames = shader.getTextureUniformNames();

        if (batched)
            batchedTagSpriteData.put(tag, new BatchedTagSpriteData(tag, vertexAttributes, spriteBatchSize, shaderProperties, textureUniformNames));
        else {
            nonBatchedTagSpriteData.put(tag, new NonBatchedTagSpriteData(tag, vertexAttributes, shaderProperties));
            nonBatchedSpritesByTag.put(tag, new ObjectSet<GraphSprite>());
        }

        tagPropertyContainers.put(tag, new MapWritablePropertyContainer());
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }

    @Override
    public void dispose() {
        for (BatchedTagSpriteData spriteData : batchedTagSpriteData.values()) {
            spriteData.dispose();
        }
        for (NonBatchedTagSpriteData spriteData : nonBatchedTagSpriteData.values()) {
            spriteData.dispose();
        }
    }
}
