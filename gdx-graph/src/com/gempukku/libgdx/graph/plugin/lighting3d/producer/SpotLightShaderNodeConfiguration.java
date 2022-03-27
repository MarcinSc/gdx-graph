package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SpotLightShaderNodeConfiguration extends NodeConfigurationImpl {
    public SpotLightShaderNodeConfiguration() {
        super("SpotLight", "Spot light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new GraphNodeOutputImpl("intensity", "Intensity", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("cutOffAngle", "Cut off angle", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("exponent", "Exponent", ShaderFieldType.Float));
    }
}
