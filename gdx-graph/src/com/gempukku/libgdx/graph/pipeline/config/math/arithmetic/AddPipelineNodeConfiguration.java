package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class AddPipelineNodeConfiguration extends NodeConfigurationImpl {
    public AddPipelineNodeConfiguration() {
        super("Add", "Add", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl("inputs", "Inputs", true, false, true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction(Float, "inputs"),
                        Float, Vector2, Vector3, Color));
    }
}
