package com.gempukku.libgdx.graph.shader.config.common.math.arithmetic;

import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SubtractShaderNodeConfiguration extends NodeConfigurationImpl {
    public SubtractShaderNodeConfiguration() {
        super("Subtract", "Subtract", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new VectorArithmeticOutputTypeFunction(ShaderFieldType.Float, "a", "b"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
