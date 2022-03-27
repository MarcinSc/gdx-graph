package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector2;

public class ValueVector2ShaderNodeConfiguration extends NodeConfigurationImpl {
    public ValueVector2ShaderNodeConfiguration() {
        super("ValueVector2", "Vector2", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", Vector2));
    }
}
