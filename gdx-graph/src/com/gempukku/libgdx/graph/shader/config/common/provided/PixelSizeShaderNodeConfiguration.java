package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PixelSizeShaderNodeConfiguration extends NodeConfigurationImpl {
    public PixelSizeShaderNodeConfiguration() {
        super("PixelSize", "Pixel size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("size", "Size", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("y", "Y", ShaderFieldType.Float));
    }
}
