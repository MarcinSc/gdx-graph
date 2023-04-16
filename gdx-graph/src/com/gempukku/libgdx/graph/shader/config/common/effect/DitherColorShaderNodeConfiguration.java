package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class DitherColorShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DitherColorShaderNodeConfiguration() {
        super("DitherColor", "Dither color", "Effect");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("color", "Color", true, ShaderFieldType.Vector4));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Color", ShaderFieldType.Vector4));
    }
}
