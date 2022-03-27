package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class GradientShaderNodeConfiguration extends NodeConfigurationImpl {
    public GradientShaderNodeConfiguration() {
        super("Gradient", "Gradient", "Effect");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Color", ShaderFieldType.Vector4));
    }
}
