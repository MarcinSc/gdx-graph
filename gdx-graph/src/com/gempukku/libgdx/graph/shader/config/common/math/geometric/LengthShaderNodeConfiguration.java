package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class LengthShaderNodeConfiguration extends NodeConfigurationImpl {
    public LengthShaderNodeConfiguration() {
        super("Length", "Length", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
