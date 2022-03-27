package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class WorldPositionShaderNodeConfiguration extends NodeConfigurationImpl {
    public WorldPositionShaderNodeConfiguration() {
        super("WorldPosition", "Fragment world position", "Model");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Vector3));
    }
}
