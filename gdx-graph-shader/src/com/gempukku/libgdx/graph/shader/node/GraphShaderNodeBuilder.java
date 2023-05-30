package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public interface GraphShaderNodeBuilder {
    String getType();

    NodeConfiguration getConfiguration(JsonValue data);

    ObjectMap<String, ? extends FieldOutput> buildVertexNode(
            boolean designTime, String nodeId, JsonValue data,
            ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder,
            GraphShader graphShader, PipelineRendererConfiguration configuration);

    ObjectMap<String, ? extends FieldOutput> buildFragmentNode(
            boolean designTime, String nodeId, JsonValue data,
            ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShader graphShader, PipelineRendererConfiguration configuration);

    interface FieldOutput {
        ShaderFieldType getFieldType();

        String getRepresentation();

        String toString();
    }

    interface TextureFieldOutput extends FieldOutput {
        String getSamplerRepresentation();

        Texture.TextureWrap getUWrap();

        Texture.TextureWrap getVWrap();
    }
}
