package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class SubtractPipelineNodeConfiguration extends NodeConfigurationImpl {
    public SubtractPipelineNodeConfiguration() {
        super("Subtract", "Subtract", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl("inputA", "A", true,
                        Color, Vector3, Vector2, Float));
        addNodeInput(
                new GraphNodeInputImpl("inputB", "B", true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new VectorArithmeticOutputTypeFunction(Float, "inputA", "inputB"),
                        Float, Vector2, Vector3, Color));
    }
}
