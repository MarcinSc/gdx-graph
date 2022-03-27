package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AmbientLightShaderNodeConfiguration extends NodeConfigurationImpl {
    public AmbientLightShaderNodeConfiguration() {
        super("AmbientLight", "Ambient light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl("ambient", "Color", ShaderFieldType.Vector4));
    }
}
