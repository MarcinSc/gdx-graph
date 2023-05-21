package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class SpotLightShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SpotLightShaderNodeConfiguration() {
        super("SpotLight", "Spot light", "Lighting");
        addNodeOutput(
                new DefaultGraphNodeOutput("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new DefaultGraphNodeOutput("intensity", "Intensity", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("cutOffAngle", "Cut off angle", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("exponent", "Exponent", ShaderFieldType.Float));
    }
}
