package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class TimeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public TimeShaderNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("time", "Time", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("sinTime", "sin(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("cosTime", "cos(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("deltaTime", "deltaTime", ShaderFieldType.Float));
    }
}
