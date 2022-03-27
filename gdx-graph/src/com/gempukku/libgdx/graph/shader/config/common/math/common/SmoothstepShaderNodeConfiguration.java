package com.gempukku.libgdx.graph.shader.config.common.math.common;

import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SmoothstepShaderNodeConfiguration extends NodeConfigurationImpl {
    public SmoothstepShaderNodeConfiguration() {
        super("Smoothstep", "Smoothstep", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl("edge0", "Edge 0", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("edge1", "Edge 1", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new MathCommonOutputTypeFunction(ShaderFieldType.Float, new String[]{"input"}, new String[]{"edge0", "edge1"}),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
