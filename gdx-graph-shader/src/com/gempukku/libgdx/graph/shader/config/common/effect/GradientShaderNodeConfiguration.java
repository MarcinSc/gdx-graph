package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class GradientShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public GradientShaderNodeConfiguration() {
        super("Gradient", "Gradient", "Effect");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Color", ShaderFieldType.Vector4));
    }
}
