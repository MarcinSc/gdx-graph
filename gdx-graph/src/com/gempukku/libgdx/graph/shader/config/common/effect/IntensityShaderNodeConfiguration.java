package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class IntensityShaderNodeConfiguration extends NodeConfigurationImpl {
    public IntensityShaderNodeConfiguration() {
        super("Intensity", "Intensity (Luma)", "Effect");
        addNodeInput(
                new GraphNodeInputImpl("color", "Color", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Intensity", ShaderFieldType.Float));
    }
}
