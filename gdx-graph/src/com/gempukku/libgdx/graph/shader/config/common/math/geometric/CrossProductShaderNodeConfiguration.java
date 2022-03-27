package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class CrossProductShaderNodeConfiguration extends NodeConfigurationImpl {
    public CrossProductShaderNodeConfiguration() {
        super("CrossProduct", "Cross product", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Vector3));
    }
}
