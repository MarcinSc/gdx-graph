package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DitherShaderNodeConfiguration extends NodeConfigurationImpl {
    public DitherShaderNodeConfiguration() {
        super("Dither", "Dither", "Effect");
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Float));
    }
}
