package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class RemapValueShaderNodeConfiguration extends NodeConfigurationImpl {
    public RemapValueShaderNodeConfiguration() {
        super("RemapValue", "Remap value", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
