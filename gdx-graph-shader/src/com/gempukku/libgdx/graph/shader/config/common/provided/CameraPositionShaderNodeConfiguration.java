package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class CameraPositionShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public CameraPositionShaderNodeConfiguration() {
        super("CameraPosition", "Camera position", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("position", "Position", ShaderFieldType.Vector3));
    }
}
