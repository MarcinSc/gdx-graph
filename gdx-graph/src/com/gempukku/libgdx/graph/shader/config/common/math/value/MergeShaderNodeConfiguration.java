package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class MergeShaderNodeConfiguration extends NodeConfigurationImpl {
    public MergeShaderNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("x", "X", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("y", "Y", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("z", "Z", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("w", "W", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("v2", "Vector2", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("v3", "Vector3", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
    }
}
