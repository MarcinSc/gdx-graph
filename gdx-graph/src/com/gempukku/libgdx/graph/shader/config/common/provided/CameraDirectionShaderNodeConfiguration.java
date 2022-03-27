package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class CameraDirectionShaderNodeConfiguration extends NodeConfigurationImpl {
    public CameraDirectionShaderNodeConfiguration() {
        super("CameraDirection", "Camera direction", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("direction", "Direction", ShaderFieldType.Vector3));
    }
}
