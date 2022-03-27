package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class BorderDetectionShaderNodeConfiguration extends NodeConfigurationImpl {
    public BorderDetectionShaderNodeConfiguration() {
        super("BorderDetection", "Border detection", "Texture");
        addNodeInput(
                new GraphNodeInputImpl("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("outlineWidth", "Outline width", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alphaEdge", "Alpha edge", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("left", "Left", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("right", "Right", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("up", "Up", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("down", "Down", ShaderFieldType.Float));
    }
}
