package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class CameraViewportSizeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public CameraViewportSizeShaderNodeConfiguration() {
        super("CameraViewportSize", "Camera viewport size", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("viewport", "Viewport", ShaderFieldType.Vector2));
    }
}
