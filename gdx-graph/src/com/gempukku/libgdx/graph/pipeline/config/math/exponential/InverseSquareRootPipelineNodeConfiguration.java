package com.gempukku.libgdx.graph.pipeline.config.math.exponential;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class InverseSquareRootPipelineNodeConfiguration extends NodeConfigurationImpl {
    public InverseSquareRootPipelineNodeConfiguration() {
        super("Inversesqrt", "Inverse square root", "Math/Exponential");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new SameTypeOutputTypeFunction("input"),
                        PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
    }
}
