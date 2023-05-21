package com.gempukku.libgdx.graph.ui.preview;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.PropertiesRenderableModel;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class SimplePreviewRenderableModel implements PreviewRenderableModel, Disposable {
    private final int vertexCount;
    private final short[] indices;
    private final ObjectSet<String> tags = new ObjectSet<>();
    private PropertiesRenderableModel propertiesRenderableModel;
    private HierarchicalPropertyContainer hierarchicalPropertyContainer;

    public SimplePreviewRenderableModel(int vertexCount, short[] indices) {
        this.vertexCount = vertexCount;

        this.indices = new short[indices.length];
        System.arraycopy(indices, 0, this.indices, 0, indices.length);

        this.hierarchicalPropertyContainer = new HierarchicalPropertyContainer();
    }

    public void addProperty(String name, Object value) {
        hierarchicalPropertyContainer.setValue(name, value);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public void updateModel(ObjectMap<String, BasicShader.Attribute> attributeMap,
                            ObjectMap<String, ShaderPropertySource> propertySourceMap, PropertyContainer propertyContainer) {
        if (propertiesRenderableModel != null)
            propertiesRenderableModel.dispose();

        hierarchicalPropertyContainer.setParent(propertyContainer);

        VertexAttributes vertexAttributes = GraphModelUtil.getVertexAttributes(attributeMap);
        ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(vertexAttributes, propertySourceMap);

        propertiesRenderableModel = new PropertiesRenderableModel(
                vertexAttributes, vertexPropertySources, vertexCount, indices, hierarchicalPropertyContainer);
    }

    @Override
    public Vector3 getPosition() {
        return propertiesRenderableModel.getPosition();
    }

    @Override
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return tags.contains(graphShader.getTag()) && propertiesRenderableModel.isRendered(graphShader, camera);
    }

    @Override
    public Matrix4 getWorldTransform() {
        return propertiesRenderableModel.getWorldTransform();
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertiesRenderableModel.getPropertyContainer();
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {
        propertiesRenderableModel.prepareToRender(shaderContext);
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        propertiesRenderableModel.render(camera, shaderProgram, propertyToLocationMapping);
    }

    @Override
    public void dispose() {
        if (propertiesRenderableModel != null) {
            propertiesRenderableModel.dispose();
        }
    }
}