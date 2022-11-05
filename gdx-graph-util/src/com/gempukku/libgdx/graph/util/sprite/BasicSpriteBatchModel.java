package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.manager.LimitedCapacitySpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class BasicSpriteBatchModel implements SpriteBatchModel {
    private SpriteRenderableModel delegate;
    private GraphModels graphModels;
    private String tag;

    public BasicSpriteBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag) {
        this(staticBatch, spriteCapacity, graphModels, tag, new MapWritablePropertyContainer());
    }

    public BasicSpriteBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag,
                                 WritablePropertyContainer propertyContainer) {
        this(staticBatch, spriteCapacity, graphModels, tag, propertyContainer, new QuadSpriteModel());
    }

    public BasicSpriteBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag,
                                 WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.graphModels = graphModels;
        this.tag = tag;

        VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        ObjectMap<VertexAttribute, PropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        delegate = new LimitedCapacitySpriteRenderableModel(staticBatch, spriteCapacity,
                vertexAttributes, vertexPropertySources, propertyContainer, spriteModel);
        graphModels.addModel(tag, delegate);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return delegate.getPropertyContainer();
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        return delegate.addSprite(sprite);
    }

    @Override
    public boolean hasSprite(RenderableSprite sprite) {
        return delegate.hasSprite(sprite);
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        return delegate.removeSprite(sprite);
    }

    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        return delegate.updateSprite(sprite);
    }

    @Override
    public int getSpriteCount() {
        return delegate.getSpriteCount();
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        delegate.setCullingTest(cullingTest);
    }

    @Override
    public void setPosition(Vector3 position) {
        delegate.setPosition(position);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        delegate.setWorldTransform(worldTransform);
    }

    @Override
    public void dispose() {
        graphModels.removeModel(tag, delegate);
        delegate.dispose();
    }
}
