package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class CameraViewportSizeShaderNodeConfiguration extends NodeConfigurationImpl {
    public CameraViewportSizeShaderNodeConfiguration() {
        super("CameraViewportSize", "Camera viewport size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("viewport", "Viewport", ShaderFieldType.Vector2));
    }
}
