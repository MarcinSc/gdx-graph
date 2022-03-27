package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class CheckerboardShapeShaderNodeConfiguration extends NodeConfigurationImpl {
    public CheckerboardShapeShaderNodeConfiguration() {
        super("CheckerboardShape", "Checkerboard Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("repeat", "Repeat", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Float));
    }
}
