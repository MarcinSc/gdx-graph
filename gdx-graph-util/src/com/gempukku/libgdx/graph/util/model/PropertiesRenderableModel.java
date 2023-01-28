package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.ValuePerVertex;

public class PropertiesRenderableModel implements RenderableModel, Disposable {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;
    private PropertyContainer propertyContainer;
    private final Mesh mesh;
    private final float[] vertexData;
    private int[] attributeLocations;

    public PropertiesRenderableModel(VertexAttributes vertexAttributes,
                                     ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
                                     int vertexCount, short[] indices, PropertyContainer propertyContainer) {
        this.vertexAttributes = vertexAttributes;

        this.vertexPropertySources = vertexPropertySources;
        this.propertyContainer = propertyContainer;

        vertexData = createVertexData(vertexAttributes, vertexPropertySources, vertexCount, propertyContainer);

        mesh = new Mesh(true, true, vertexCount, indices.length, vertexAttributes);
        mesh.setVertices(vertexData);

        mesh.setIndices(indices, 0, indices.length);
    }

    private static float[] createVertexData(VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
                                            int vertexCount, PropertyContainer propertyContainer) {
        int floatCountPerVertex = vertexAttributes.vertexSize / 4;
        float[] vertexData = new float[floatCountPerVertex * vertexCount];

        int arrayIndex = 0;
        for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            for (VertexAttribute vertexAttribute : vertexAttributes) {
                ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
                ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldTypeForAttribute(vertexAttribute.alias);
                Object attributeValue = propertyContainer.getValue(shaderPropertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                    shaderFieldType.setValueInAttributesArray(vertexData, arrayIndex, shaderPropertySource.getValueToUse(vertexValue));
                } else {
                    attributeValue = shaderPropertySource.getValueToUseForAttribute(vertexAttribute.alias, attributeValue);
                    shaderFieldType.setValueInAttributesArray(vertexData, arrayIndex, attributeValue);
                }
                arrayIndex += vertexAttribute.numComponents;
            }
        }
        return vertexData;
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {

    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public Matrix4 getWorldTransform() {
        return worldTransform;
    }

    @Override
    public boolean isRendered(Camera camera) {
        return true;
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);
        mesh.bind(shaderProgram, attributeLocations);
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
        mesh.unbind(shaderProgram, attributeLocations);
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
