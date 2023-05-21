package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class PointLightShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public PointLightShaderNodeConfiguration() {
        super("PointLight", "Point light", "Lighting");
        addNodeOutput(
                new DefaultGraphNodeOutput("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new DefaultGraphNodeOutput("intensity", "Intensity", ShaderFieldType.Float));
    }
}
