package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.Producer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMeshStorage;
import com.gempukku.libgdx.graph.util.storage.LimitedCapacityObjectRenderableModel;
import com.gempukku.libgdx.graph.util.storage.ObjectBatchModel;
import com.gempukku.libgdx.graph.util.storage.ObjectRenderableModel;

public class BasicObjectBatchModel implements ObjectBatchModel<RenderableSprite, SpriteReference> {
    private final ObjectRenderableModel<RenderableSprite, SpriteReference> delegate;
    private final GraphModels graphModels;
    private final String tag;

    public BasicObjectBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag) {
        this(staticBatch, spriteCapacity, graphModels, tag, new MapWritablePropertyContainer());
    }

    public BasicObjectBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag,
                                 WritablePropertyContainer propertyContainer) {
        this(staticBatch, spriteCapacity, graphModels, tag, propertyContainer, new QuadSpriteModel());
    }

    public BasicObjectBatchModel(boolean staticBatch, int spriteCapacity,
                                 GraphModels graphModels, String tag,
                                 WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.graphModels = graphModels;
        this.tag = tag;

        VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        delegate = new LimitedCapacityObjectRenderableModel<>(staticBatch,
                new SpriteSlotMeshStorage<>(spriteCapacity, spriteModel,
                        new SpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel),
                        new Producer<SpriteReference>() {
                            @Override
                            public SpriteReference create() {
                                return new SpriteReference();
                            }
                        }),
                vertexAttributes, propertyContainer);
        graphModels.addModel(tag, delegate);
    }

    @Override
    public boolean canStore(RenderableSprite sprite) {
        return delegate.canStore(sprite);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return delegate.getPropertyContainer();
    }

    @Override
    public SpriteReference addObject(RenderableSprite sprite) {
        return delegate.addObject(sprite);
    }

    @Override
    public boolean containsObject(SpriteReference objectReference) {
        return delegate.containsObject(objectReference);
    }

    @Override
    public void removeObject(SpriteReference objectReference) {
        delegate.removeObject(objectReference);
    }

    @Override
    public SpriteReference updateObject(RenderableSprite sprite, SpriteReference objectReference) {
        return delegate.updateObject(sprite, objectReference);
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
