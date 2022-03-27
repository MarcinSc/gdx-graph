package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class Matrix4FieldType implements ShaderFieldType, PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Matrix4;
    }

    @Override
    public String getShaderType() {
        return "mat4";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Matrix4";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        float[] values = new float[]{
                data.getFloat("x1"), data.getFloat("x2"), data.getFloat("x3"), data.getFloat("x4"),
                data.getFloat("y1"), data.getFloat("y2"), data.getFloat("y3"), data.getFloat("y4"),
                data.getFloat("z1"), data.getFloat("z2"), data.getFloat("z3"), data.getFloat("z4"),
                data.getFloat("w1"), data.getFloat("w2"), data.getFloat("w3"), data.getFloat("w4"),
        };
        return new Matrix4(values);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String variableName = "u_property_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        shader.setUniform(location, (Matrix4) value);
                    }
                }, "Matrix4 property - " + propertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String variableName = "u_property_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        shader.setUniform(location, (Matrix4) value);
                    }
                }, "Matrix4 property - " + propertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 16, attributeName), getShaderType(), "Matrix4 property - " + propertySource.getPropertyName());

        return new DefaultFieldOutput(getName(), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 16, attributeName), getShaderType(), "Matrix4 property - " + propertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, getShaderType());
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, getShaderType());
        }

        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        com.badlogic.gdx.math.Matrix4 matrix = (Matrix4) value;
        System.arraycopy(matrix.val, 0, vertices, startIndex, 16);
    }

    @Override
    public boolean equals(Object obj) {
        return getName().equals(((FieldType) obj).getName());
    }
}
