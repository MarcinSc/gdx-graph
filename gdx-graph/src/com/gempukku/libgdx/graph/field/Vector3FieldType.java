package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.math.Vector3;
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
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.ValueOperations;

public class Vector3FieldType implements ShaderFieldType, PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Vector3;
    }

    @Override
    public String getShaderType() {
        return "vec3";
    }

    @Override
    public int getNumberOfComponents() {
        return 3;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return ShaderFieldType.Vector3;
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        final float z = data.getFloat("z");
        return new com.badlogic.gdx.math.Vector3(x, y, z);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        String variableName = shaderPropertySource.getUniformName();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(shaderPropertySource.getPropertyName());
                        value = shaderPropertySource.getValueToUse(value);
                        shader.setUniform(location, (Vector3) value);
                    }
                }, "Vector3 property - " + shaderPropertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        String variableName = shaderPropertySource.getUniformName();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(shaderPropertySource.getPropertyName());
                        value = shaderPropertySource.getValueToUse(value);
                        shader.setUniform(location, (Vector3) value);
                    }
                }, "Vector3 property - " + shaderPropertySource.getPropertyName());
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource) {
        String attributeName = shaderPropertySource.getAttributeName();

        vertexShaderBuilder.addAttributeVariable(attributeName, getNumberOfComponents(), getShaderType(), "Vector3 property - " + shaderPropertySource.getPropertyName());

        return new DefaultFieldOutput(getName(), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource) {
        String attributeName = shaderPropertySource.getAttributeName();
        String variableName = shaderPropertySource.getVariableName();

        vertexShaderBuilder.addAttributeVariable(attributeName, getNumberOfComponents(), getShaderType(), "Vector3 property - " + shaderPropertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, getShaderType());
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, getShaderType());
        }

        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        ValueOperations.copyVector3IntoArray((Vector3) value, vertices, startIndex);
    }

    @Override
    public boolean equals(Object obj) {
        return getName().equals(((FieldType) obj).getName());
    }
}
