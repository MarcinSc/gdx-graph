package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class Vector4ShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.graphics.Color;
    }

    @Override
    public String getShaderType() {
        return "vec4";
    }

    @Override
    public int getNumberOfComponents() {
        return 4;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Vector4";
    }

    @Override
    public boolean isTexture() {
        return false;
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
                        shader.setUniform(location, (Color) value);
                    }
                }, "Vector4 property - " + propertySource.getPropertyName());
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
                        shader.setUniform(location, (Color) value);
                    }
                }, "Vector4 property - " + propertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, attributeName), getShaderType(), "Vector4 property - " + propertySource.getPropertyName());

        return new DefaultFieldOutput(getName(), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, attributeName), getShaderType(), "Vector4 property - " + propertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, getShaderType());
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, getShaderType());
        }

        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        vertices[startIndex + 0] = ((Color) value).r;
        vertices[startIndex + 1] = ((Color) value).g;
        vertices[startIndex + 2] = ((Color) value).b;
        vertices[startIndex + 3] = ((Color) value).a;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return Color.valueOf(data.getString("color"));
    }
}
