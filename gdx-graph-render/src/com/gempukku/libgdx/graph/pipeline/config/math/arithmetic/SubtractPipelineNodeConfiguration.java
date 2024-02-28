package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class SubtractPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SubtractPipelineNodeConfiguration() {
        super("Subtract", "Subtract", "Math/Arithmetic");
        addNodeInput(
                new DefaultGraphNodeInput("inputA", "A", true,
                        Color, Vector3, Vector2, Float));
        addNodeInput(
                new DefaultGraphNodeInput("inputB", "B", true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new VectorArithmeticOutputTypeFunction(Float, "inputA", "inputB"),
                        Float, Vector2, Vector3, Color));
    }
}
