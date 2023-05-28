package com.gempukku.libgdx.graph.shader.preview;

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
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.PropertiesRenderableModel;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class MapPreviewRenderableModel implements PreviewRenderableModel, Disposable {
    private final int vertexCount;
    private final short[] indices;
    private final ObjectSet<String> tags = new ObjectSet<>();
    private final HierarchicalPropertyContainer hierarchicalPropertyContainer;
    private PropertiesRenderableModel propertiesRenderableModel;

    private final ObjectMap<String, Object> attributeFunctionValues = new ObjectMap<>();

    public MapPreviewRenderableModel(int vertexCount, short[] indices) {
        this.vertexCount = vertexCount;

        this.indices = new short[indices.length];
        System.arraycopy(indices, 0, this.indices, 0, indices.length);

        this.hierarchicalPropertyContainer = new HierarchicalPropertyContainer();
    }

    public void addAttributeFunctionValue(String attributeFunction, Object value) {
        attributeFunctionValues.put(attributeFunction, value);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    private void fillPropertyContainerBasedOnAttributeFunctions(ObjectMap<String, ShaderPropertySource> propertySourceMap) {
        hierarchicalPropertyContainer.clear();

        for (ObjectMap.Entry<String, ShaderPropertySource> stringShaderPropertySourceEntry : propertySourceMap) {
            String name = stringShaderPropertySourceEntry.key;
            ShaderPropertySource property = stringShaderPropertySourceEntry.value;;
            if (property.getPropertyLocation() == PropertyLocation.Attribute) {
                String function = property.getAttributeFunction();
                if (function != null) {
                    Object value = getValueForAttributeFunction(function);
                    if (value != null) {
                        hierarchicalPropertyContainer.setValue(name, value);
                    }
                }
            }
        }
    }

    private Object getValueForAttributeFunction(String attributeFunction) {
        return attributeFunctionValues.get(attributeFunction);
    }

    @Override
    public void updateModel(ObjectMap<String, BasicShader.Attribute> attributeMap,
                            ObjectMap<String, ShaderPropertySource> propertySourceMap, PropertyContainer propertyContainer) {
        if (propertiesRenderableModel != null) {
            propertiesRenderableModel.dispose();
            propertiesRenderableModel = null;
        }

        try {
            fillPropertyContainerBasedOnAttributeFunctions(propertySourceMap);
            hierarchicalPropertyContainer.setParent(propertyContainer);

            VertexAttributes vertexAttributes = GraphModelUtil.getVertexAttributes(attributeMap);
            ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(vertexAttributes, propertySourceMap);

            propertiesRenderableModel = new PropertiesRenderableModel(
                    vertexAttributes, vertexPropertySources, vertexCount, indices, hierarchicalPropertyContainer);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
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
    public void update(float currentTime) {

    }

    @Override
    public void dispose() {
        if (propertiesRenderableModel != null) {
            propertiesRenderableModel.dispose();
            propertiesRenderableModel = null;
        }
    }
}
