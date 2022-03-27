package com.gempukku.libgdx.graph.shader.config.common.math.trigonometry;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ArccosShaderNodeConfiguration extends NodeConfigurationImpl {
    public ArccosShaderNodeConfiguration() {
        super("Arccos", "Arccosine", "Math/Trigonometry");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new SameTypeOutputTypeFunction("input"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
