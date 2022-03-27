package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class DistancePipelineNodeConfiguration extends NodeConfigurationImpl {
    public DistancePipelineNodeConfiguration() {
        super("Distance", "Distance", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("p0", "Point 0", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("p1", "Point 1", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(PipelineFieldType.Float, "p0", "p1"),
                        PipelineFieldType.Float));
    }
}
