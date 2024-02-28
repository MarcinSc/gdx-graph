package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class IntensityShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public IntensityShaderNodeConfiguration() {
        super("Intensity", "Intensity (Luma)", "Effect");
        addNodeInput(
                new DefaultGraphNodeInput("color", "Color", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Intensity", ShaderFieldType.Float));
    }
}
