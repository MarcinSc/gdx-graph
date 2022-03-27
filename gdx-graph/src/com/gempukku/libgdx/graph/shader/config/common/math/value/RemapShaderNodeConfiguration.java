package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class RemapShaderNodeConfiguration extends NodeConfigurationImpl {
    public RemapShaderNodeConfiguration() {
        super("Remap", "Remap", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("from", "From", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("to", "To", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new SameTypeOutputTypeFunction("input"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
