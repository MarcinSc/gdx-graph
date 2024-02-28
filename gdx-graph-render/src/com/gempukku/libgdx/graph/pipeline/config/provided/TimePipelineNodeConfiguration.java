package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;

public class TimePipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public TimePipelineNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("time", "Time", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("sinTime", "sin(Time)", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("cosTime", "cos(Time)", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("deltaTime", "deltaTime", Float));
    }
}
