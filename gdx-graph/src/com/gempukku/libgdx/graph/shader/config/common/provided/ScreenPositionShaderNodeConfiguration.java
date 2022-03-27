package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ScreenPositionShaderNodeConfiguration extends NodeConfigurationImpl {
    public ScreenPositionShaderNodeConfiguration() {
        super("ScreenPosition", "Screen position", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Vector2));
    }
}
