package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class FresnelEffectShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public FresnelEffectShaderNodeConfiguration() {
        super("FresnelEffect", "Fresnel effect", "Effect");
        addNodeInput(
                new DefaultGraphNodeInput("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("viewDir", "View direction", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("power", "Power", true, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Float));
    }
}
