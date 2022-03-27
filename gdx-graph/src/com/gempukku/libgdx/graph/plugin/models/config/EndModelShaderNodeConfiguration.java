package com.gempukku.libgdx.graph.plugin.models.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EndModelShaderNodeConfiguration extends NodeConfigurationImpl {
    public EndModelShaderNodeConfiguration() {
        super("ShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("color", "Color", false, false, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
