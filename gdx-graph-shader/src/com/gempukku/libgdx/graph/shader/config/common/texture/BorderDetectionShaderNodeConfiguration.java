package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class BorderDetectionShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public BorderDetectionShaderNodeConfiguration() {
        super("BorderDetection", "Border detection", "Texture");
        addNodeInput(
                new DefaultGraphNodeInput("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("outlineWidth", "Outline width", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alphaEdge", "Alpha edge", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("left", "Left", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("right", "Right", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("up", "Up", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("down", "Down", ShaderFieldType.Float));
    }
}
