package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;

public class TimePipelineNodeConfiguration extends NodeConfigurationImpl {
    public TimePipelineNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("time", "Time", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("sinTime", "sin(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("cosTime", "cos(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("deltaTime", "deltaTime", Float));
    }
}
