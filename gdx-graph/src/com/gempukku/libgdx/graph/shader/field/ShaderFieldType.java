package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface ShaderFieldType extends FieldType {
    String Float = "Float";
    String Vector2 = "Vector2";
    String Vector3 = "Vector3";
    String Vector4 = "Vector4";
    String Boolean = "Boolean";
    String TextureRegion = "TextureRegion";
    String Matrix4 = "Matrix4";

    String getShaderType();

    int getNumberOfComponents();

    Object convertFromJson(JsonValue data);

    GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    void setValueInAttributesArray(float[] vertices, int startIndex, Object value);
}
