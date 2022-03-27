package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DotProductShaderNodeConfiguration extends NodeConfigurationImpl {
    public DotProductShaderNodeConfiguration() {
        super("DotProduct", "Dot product", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(ShaderFieldType.Float, "a", "b"),
                        ShaderFieldType.Float));
    }
}
