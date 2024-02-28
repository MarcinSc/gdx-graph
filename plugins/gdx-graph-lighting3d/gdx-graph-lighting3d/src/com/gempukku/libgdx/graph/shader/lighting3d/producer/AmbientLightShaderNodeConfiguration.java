package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class AmbientLightShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public AmbientLightShaderNodeConfiguration() {
        super("AmbientLight", "Ambient light", "Lighting");
        addNodeOutput(
                new DefaultGraphNodeOutput("ambient", "Color", ShaderFieldType.Vector4));
    }
}
