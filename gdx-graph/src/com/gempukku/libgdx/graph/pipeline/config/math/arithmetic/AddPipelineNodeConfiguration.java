package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class AddPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public AddPipelineNodeConfiguration() {
        super("Add", "Add", "Math/Arithmetic");
        addNodeInput(
                new DefaultGraphNodeInput("inputs", "Inputs", true, false, true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction(Float, "inputs"),
                        Float, Vector2, Vector3, Color));
    }
}
