package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class SimpleShaderRendererConfiguration implements ShaderRendererConfiguration<RenderableModel>, ShaderInformation {
    private PropertyContainer rootPropertyContainer;
    private ObjectMap<String, ObjectMap<String, BasicShader.Attribute>> shaderAttributes = new ObjectMap<>();
    private ObjectMap<String, ObjectMap<String, ShaderPropertySource>> shaderProperties = new ObjectMap<>();
    private ObjectMap<String, WritablePropertyContainer> globalUniforms = new ObjectMap<>();

    private FallbackPropertyContainer fallbackPropertyContainer = new FallbackPropertyContainer();

    private ObjectSet<RenderableModel> models = new ObjectSet<>();

    public SimpleShaderRendererConfiguration(PropertyContainer rootPropertyContainer) {
        this.rootPropertyContainer = rootPropertyContainer;
    }

    @Override
    public ObjectMap<String, BasicShader.Attribute> getShaderAttributes(String tag) {
        return shaderAttributes.get(tag);
    }

    @Override
    public ObjectMap<String, ShaderPropertySource> getShaderProperties(String tag) {
        return shaderProperties.get(tag);
    }

    @Override
    public void registerShader(GraphShader graphShader) {
        String tag = graphShader.getTag();
        shaderAttributes.put(tag, graphShader.getAttributes());
        shaderProperties.put(tag, graphShader.getProperties());
        HierarchicalPropertyContainer shaderGlobalUniforms = new HierarchicalPropertyContainer(rootPropertyContainer);
        globalUniforms.put(tag, shaderGlobalUniforms);
    }

    public WritablePropertyContainer getGlobalUniforms(String tag) {
        return globalUniforms.get(tag);
    }

    @Override
    public WritablePropertyContainer getGlobalUniforms(GraphShader graphShader) {
        return getGlobalUniforms(graphShader.getTag());
    }

    @Override
    public PropertyContainer getModelUniforms(RenderableModel renderableModel, GraphShader graphShader) {
        fallbackPropertyContainer.setFallback(getGlobalUniforms(graphShader));
        fallbackPropertyContainer.setMain(renderableModel.getPropertyContainer());
        return fallbackPropertyContainer;
    }

    @Override
    public Vector3 getPosition(RenderableModel renderableModel, GraphShader graphShader) {
        return renderableModel.getPosition();
    }

    @Override
    public Matrix4 getWorldTransform(RenderableModel renderableModel, GraphShader graphShader) {
        return renderableModel.getWorldTransform();
    }

    @Override
    public boolean isRendered(RenderableModel renderableModel, GraphShader shader, Camera camera) {
        return renderableModel.isRendered(shader, camera);
    }

    @Override
    public void renderModel(RenderableModel renderableModel, ShaderContext shaderContext, IntMapping<String> propertyToLocationMapping) {
        renderableModel.render(shaderContext.getCamera(), shaderContext.getGraphShader().getShaderProgram(), propertyToLocationMapping);
    }

    @Override
    public void addModel(RenderableModel renderableModel) {
        models.add(renderableModel);
    }

    @Override
    public void removeModel(RenderableModel renderableModel) {
        models.remove(renderableModel);
    }

    @Override
    public Iterable<? extends RenderableModel> getModels() {
        return models;
    }

    @Override
    public void removeAllModels() {
        models.clear();
    }
}
