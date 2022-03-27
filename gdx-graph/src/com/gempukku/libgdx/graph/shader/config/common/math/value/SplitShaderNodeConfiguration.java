package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SplitShaderNodeConfiguration extends NodeConfigurationImpl {
    public SplitShaderNodeConfiguration() {
        super("Split", "Split", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("y", "Y", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("z", "Z", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("w", "W", ShaderFieldType.Float));
    }
}
