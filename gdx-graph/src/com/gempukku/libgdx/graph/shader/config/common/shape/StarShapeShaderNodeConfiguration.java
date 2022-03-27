package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class StarShapeShaderNodeConfiguration extends NodeConfigurationImpl {
    public StarShapeShaderNodeConfiguration() {
        super("StarShape", "Star Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("arms", "Arms", true, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("minDepth", "Min depth", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("maxDepth", "Max depth", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("curve", "Curve", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Float));
    }
}
