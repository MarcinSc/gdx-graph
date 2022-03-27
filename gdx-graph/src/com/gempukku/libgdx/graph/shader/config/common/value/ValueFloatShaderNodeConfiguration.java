package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ValueFloatShaderNodeConfiguration extends NodeConfigurationImpl {
    public ValueFloatShaderNodeConfiguration() {
        super("ValueFloat", "Float", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", ShaderFieldType.Float));
    }
}
