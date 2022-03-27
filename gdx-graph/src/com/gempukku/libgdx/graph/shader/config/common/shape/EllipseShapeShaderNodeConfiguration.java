package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EllipseShapeShaderNodeConfiguration extends NodeConfigurationImpl {
    public EllipseShapeShaderNodeConfiguration() {
        super("EllipseShape", "Ellipse Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("size", "Size", false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("border", "Border", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Float));
    }
}
