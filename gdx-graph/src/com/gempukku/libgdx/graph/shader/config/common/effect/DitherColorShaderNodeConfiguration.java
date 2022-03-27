package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DitherColorShaderNodeConfiguration extends NodeConfigurationImpl {
    public DitherColorShaderNodeConfiguration() {
        super("DitherColor", "Dither color", "Effect");
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("color", "Color", true, ShaderFieldType.Vector4));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Color", ShaderFieldType.Vector4));
    }
}
