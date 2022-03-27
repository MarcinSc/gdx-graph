package com.gempukku.libgdx.graph.plugin.lighting3d.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EndShadowShaderNodeConfiguration extends NodeConfigurationImpl {
    public EndShadowShaderNodeConfiguration() {
        super("ShadowShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
