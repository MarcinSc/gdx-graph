package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class RemapVectorShaderNodeConfiguration extends NodeConfigurationImpl {
    public RemapVectorShaderNodeConfiguration() {
        super("RemapVector", "Remap vector", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("v2", "Vector2", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("v3", "Vector3", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
    }
}
