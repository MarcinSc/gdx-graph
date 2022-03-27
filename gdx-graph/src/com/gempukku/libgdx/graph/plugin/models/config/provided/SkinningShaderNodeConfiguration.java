package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SkinningShaderNodeConfiguration extends NodeConfigurationImpl {
    public SkinningShaderNodeConfiguration() {
        super("Skinning", "Skinning object", "Model");
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("normal", "Normal", false, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("skinnedPosition", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("skinnedNormal", "Normal", ShaderFieldType.Vector3));
    }
}
