package com.gempukku.libgdx.graph.plugin.screen.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EndScreenShaderNodeConfiguration extends NodeConfigurationImpl {
    public EndScreenShaderNodeConfiguration() {
        super("FullScreenShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl("color", "Color", false, false,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
