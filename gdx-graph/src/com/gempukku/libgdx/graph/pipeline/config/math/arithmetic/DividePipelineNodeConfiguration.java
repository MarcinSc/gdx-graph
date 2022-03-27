package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class DividePipelineNodeConfiguration extends NodeConfigurationImpl {
    public DividePipelineNodeConfiguration() {
        super("Divide", "Divide", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new VectorArithmeticOutputTypeFunction(PipelineFieldType.Float, "a", "b"),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
