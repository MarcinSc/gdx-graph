package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ObjectNormalToWorldShaderNodeConfiguration extends NodeConfigurationImpl {
    public ObjectNormalToWorldShaderNodeConfiguration() {
        super("ObjectNormalToWorld", "Object normal to World Space", "Model");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Vector3));
    }
}
