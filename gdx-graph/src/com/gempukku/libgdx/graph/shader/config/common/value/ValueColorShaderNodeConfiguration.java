package com.gempukku.libgdx.graph.shader.config.common.value;


import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector4;

public class ValueColorShaderNodeConfiguration extends NodeConfigurationImpl {
    public ValueColorShaderNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", Vector4));
    }
}
