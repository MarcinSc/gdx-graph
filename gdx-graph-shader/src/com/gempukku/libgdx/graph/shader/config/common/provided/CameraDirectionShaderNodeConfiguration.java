package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class CameraDirectionShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public CameraDirectionShaderNodeConfiguration() {
        super("CameraDirection", "Camera direction", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("direction", "Direction", ShaderFieldType.Vector3));
    }
}
