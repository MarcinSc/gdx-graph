package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphModelsImpl implements GraphModels, RuntimePipelinePlugin {
    private final ObjectMap<String, ObjectSet<RenderableModel>> modelsByTag = new ObjectMap<>();
    private final ObjectMap<String, ObjectMap<String, PropertySource>> propertiesByTag = new ObjectMap<>();
    private final ObjectMap<String, MapWritablePropertyContainer> propertiesForTag = new ObjectMap<>();

    public void registerTag(String tag, GraphShader shader) {
        if (modelsByTag.containsKey(tag))
            throw new IllegalStateException("There is already a shader with tag: " + tag);
        modelsByTag.put(tag, new ObjectSet<RenderableModel>());
        propertiesByTag.put(tag, shader.getProperties());
        propertiesForTag.put(tag, new MapWritablePropertyContainer());
    }

    public Iterable<? extends RenderableModel> getModels(String tag) {
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
    public void addModel(String tag, RenderableModel model) {
        modelsByTag.get(tag).add(model);
    }

    @Override
    public void removeModel(String tag, RenderableModel model) {
        modelsByTag.get(tag).remove(model);
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
