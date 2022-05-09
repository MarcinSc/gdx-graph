package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphModelsImpl implements GraphModels, RuntimePipelinePlugin {
    private final ObjectMap<String, ObjectSet<GraphModel>> modelsByTag = new ObjectMap<>();
    private final ObjectMap<String, ObjectMap<String, PropertySource>> propertiesByTag = new ObjectMap<>();
    private final ObjectMap<String, MapWritablePropertyContainer> propertiesForTag = new ObjectMap<>();

    public void registerTag(String tag, GraphShader shader) {
        if (modelsByTag.containsKey(tag))
            throw new IllegalStateException("There is already a shader with tag: " + tag);
        modelsByTag.put(tag, new ObjectSet<GraphModel>());
        propertiesByTag.put(tag, shader.getProperties());
        propertiesForTag.put(tag, new MapWritablePropertyContainer());
    }

    public Iterable<? extends GraphModel> getModels(String tag) {
        return modelsByTag.get(tag);
    }

    public boolean hasModelWithTag(String tag) {
        return !modelsByTag.get(tag).isEmpty();
    }

    public PropertyContainer getGlobalProperties(String tag) {
        return propertiesForTag.get(tag);
    }

    @Override
    public ObjectMap<String, PropertySource> getShaderProperties(String tag) {
        return propertiesByTag.get(tag);
    }

    @Override
    public GraphModel addModel(String tag, RenderableModel model) {
        GraphModelImpl graphModel = new GraphModelImpl(tag, model);
        modelsByTag.get(tag).add(graphModel);
        return graphModel;
    }

    @Override
    public void removeModel(GraphModel model) {
        GraphModelImpl graphModel = (GraphModelImpl) model;
        modelsByTag.get(graphModel.getTag()).remove(graphModel);
    }

    @Override
    public void setGlobalProperty(String tag, String name, Object value) {
        propertiesForTag.get(tag).setValue(name, value);
    }

    @Override
    public void unsetGlobalProperty(String tag, String name) {
        propertiesForTag.get(tag).remove(name);
    }

    @Override
    public Object getGlobalProperty(String tag, String name) {
        return propertiesForTag.get(tag).getValue(name);
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }
}
