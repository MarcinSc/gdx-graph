package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class DirectionalLightShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DirectionalLightShaderNodeConfiguration() {
        super("DirectionalLight", "Directional light", "Lighting");
        addNodeOutput(
                new DefaultGraphNodeOutput("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
    }
}
