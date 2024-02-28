package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class DistancePipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DistancePipelineNodeConfiguration() {
        super("Distance", "Distance", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("p0", "Point 0", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("p1", "Point 1", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(PipelineFieldType.Float, "p0", "p1"),
                        PipelineFieldType.Float));
    }
}
