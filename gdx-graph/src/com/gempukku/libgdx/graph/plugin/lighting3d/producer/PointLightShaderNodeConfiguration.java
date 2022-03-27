package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PointLightShaderNodeConfiguration extends NodeConfigurationImpl {
    public PointLightShaderNodeConfiguration() {
        super("PointLight", "Point light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new GraphNodeOutputImpl("intensity", "Intensity", ShaderFieldType.Float));
    }
}
