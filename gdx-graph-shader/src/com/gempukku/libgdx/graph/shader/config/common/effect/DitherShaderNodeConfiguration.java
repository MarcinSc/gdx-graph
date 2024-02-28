package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class DitherShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DitherShaderNodeConfiguration() {
        super("Dither", "Dither", "Effect");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Float));
    }
}
