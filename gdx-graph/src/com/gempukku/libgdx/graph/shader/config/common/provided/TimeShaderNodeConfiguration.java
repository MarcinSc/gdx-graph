package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class TimeShaderNodeConfiguration extends NodeConfigurationImpl {
    public TimeShaderNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("time", "Time", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("sinTime", "sin(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("cosTime", "cos(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("deltaTime", "deltaTime", ShaderFieldType.Float));
    }
}
