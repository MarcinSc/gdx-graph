package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FresnelEffectShaderNodeConfiguration extends NodeConfigurationImpl {
    public FresnelEffectShaderNodeConfiguration() {
        super("FresnelEffect", "Fresnel effect", "Effect");
        addNodeInput(
                new GraphNodeInputImpl("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("viewDir", "View direction", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("power", "Power", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Float));
    }
}
