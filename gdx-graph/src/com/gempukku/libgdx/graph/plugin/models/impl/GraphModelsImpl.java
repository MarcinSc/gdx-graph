package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphModelsImpl implements GraphModels, RuntimePipelinePlugin {
    private final ObjectSet<RenderableModel> models = new ObjectSet<>();
    private final ObjectMap<String, GraphShader> shaderByTag = new ObjectMap<>();
    private final ObjectMap<String, MapWritablePropertyContainer> propertiesForTag = new ObjectMap<>();

    public void registerTag(String tag, GraphShader shader) {
        if (shaderByTag.containsKey(tag))
            throw new IllegalStateException("There is already a shader with tag: " + tag);
        shaderByTag.put(tag, shader);
        propertiesForTag.put(tag, new MapWritablePropertyContainer());
    }

    public Iterable<? extends RenderableModel> getModels() {
        return models;
    }

    public PropertyContainer getGlobalProperties(String tag) {
        return propertiesForTag.get(tag);
    }

    @Override
    public ObjectMap<String, ShaderPropertySource> getShaderProperties(String tag) {
        GraphShader graphShader = shaderByTag.get(tag);
        if (graphShader == null)
            return null;
        return graphShader.getProperties();
    }

    @Override
    public ObjectMap<String, BasicShader.Attribute> getShaderAttributes(String tag) {
        GraphShader graphShader = shaderByTag.get(tag);
        if (graphShader == null)
            return null;
        return graphShader.getAttributes();
    }

    @Override
    public void addModel(RenderableModel model) {
        models.add(model);
    }

    @Override
    public void removeModel(RenderableModel model) {
        models.remove(model);
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
