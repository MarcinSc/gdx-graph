package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class AddPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public AddPipelineNodeConfiguration() {
        super("Add", "Add", "Math/Arithmetic");
        addNodeInput(
                new DefaultGraphNodeInput("inputs", "Inputs", true, GraphNodeInputSide.Left, true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new VectorArithmeticOutputTypeFunction(Float, "inputs"),
                        Float, Vector2, Vector3, Color));
    }
}
