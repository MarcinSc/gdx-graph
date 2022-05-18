package com.gempukku.libgdx.graph.util.sprite.manager;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;

public class MinimumSpriteRenderableModelManager implements SpriteRenderableModelManager<LimitedCapacitySpriteRenderableModel> {
    private final int preserveMinimum;
    private final boolean staticBatch;
    private final int spriteCapacity;
    private final GraphModels graphModels;
    private final String tag;

    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, PropertySource> vertexPropertySources;

    private int modelCount = 0;

    public MinimumSpriteRenderableModelManager(int preserveMinimum, boolean staticBatch, int spriteCapacity,
                                               GraphModels graphModels, String tag) {
        this.preserveMinimum = preserveMinimum;
        this.staticBatch = staticBatch;
        this.spriteCapacity = spriteCapacity;
        this.graphModels = graphModels;
        this.tag = tag;

        vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);
    }

    @Override
    public LimitedCapacitySpriteRenderableModel createNewModel(WritablePropertyContainer propertyContainer) {
        modelCount++;
        LimitedCapacitySpriteRenderableModel model = new LimitedCapacitySpriteRenderableModel(staticBatch, spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer);
        graphModels.addModel(tag, model);
        return model;
    }

    @Override
    public boolean shouldDisposeEmptyModel(LimitedCapacitySpriteRenderableModel model) {
        return modelCount > preserveMinimum;
    }

    @Override
    public void disposeModel(LimitedCapacitySpriteRenderableModel model) {
        graphModels.removeModel(tag, model);
        model.dispose();
        modelCount--;
    }
}
