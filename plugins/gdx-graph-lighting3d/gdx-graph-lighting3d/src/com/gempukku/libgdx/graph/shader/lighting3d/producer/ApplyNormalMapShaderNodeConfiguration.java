package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ApplyNormalMapShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ApplyNormalMapShaderNodeConfiguration() {
        super("ApplyNormalMap", "Apply normal map", "Lighting");
        addNodeInput(
                new DefaultGraphNodeInput("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("tangent", "Tangent", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("normalMap", "Normal from map", true, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
        addNodeInput(
                new DefaultGraphNodeInput("strength", "Strength", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Normal", ShaderFieldType.Vector3));
    }
}
