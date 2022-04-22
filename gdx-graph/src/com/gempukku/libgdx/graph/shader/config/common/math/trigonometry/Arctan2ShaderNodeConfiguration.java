package com.gempukku.libgdx.graph.shader.config.common.math.trigonometry;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Arctan2ShaderNodeConfiguration extends NodeConfigurationImpl {
    public Arctan2ShaderNodeConfiguration() {
        super("Arctan2", "Arctangent2", "Math/Trigonometry");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
