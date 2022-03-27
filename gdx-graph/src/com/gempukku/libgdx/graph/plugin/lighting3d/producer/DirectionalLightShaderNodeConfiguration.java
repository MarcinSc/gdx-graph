package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DirectionalLightShaderNodeConfiguration extends NodeConfigurationImpl {
    public DirectionalLightShaderNodeConfiguration() {
        super("DirectionalLight", "Directional light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
    }
}
