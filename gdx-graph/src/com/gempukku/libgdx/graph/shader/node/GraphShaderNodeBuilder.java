package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public interface GraphShaderNodeBuilder {
    String getType();

    NodeConfiguration getConfiguration(JsonValue data);

    ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                             VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                               VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    interface FieldOutput {
        ShaderFieldType getFieldType();

        String getRepresentation();

        String getSamplerRepresentation();

        String toString();
    }
}
