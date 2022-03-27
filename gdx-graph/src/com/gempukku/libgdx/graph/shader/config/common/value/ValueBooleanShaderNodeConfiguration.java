package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ValueBooleanShaderNodeConfiguration extends NodeConfigurationImpl {
    public ValueBooleanShaderNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", ShaderFieldType.Boolean));
    }
}
