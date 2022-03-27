package com.gempukku.libgdx.graph.shader.config.common.math.arithmetic;

import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AddShaderNodeConfiguration extends NodeConfigurationImpl {
    public AddShaderNodeConfiguration() {
        super("Add", "Add", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl("inputs", "Inputs", true, false, true,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction(ShaderFieldType.Float, "inputs"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
