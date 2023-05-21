package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface ShaderFieldType extends FieldType {
    String Float = PipelineFieldType.Float;
    String Vector2 = PipelineFieldType.Vector2;
    String Vector3 = PipelineFieldType.Vector3;
    String Vector4 = "Vector4";
    String Boolean = PipelineFieldType.Boolean;
    String TextureRegion = "TextureRegion";
    String Matrix4 = PipelineFieldType.Matrix4;

    String getShaderType();

    int getNumberOfComponents();

    Object convertFromJson(JsonValue data);

    GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource);

    void setValueInAttributesArray(String attributeName, float[] vertices, int startIndex, Object value);
}
