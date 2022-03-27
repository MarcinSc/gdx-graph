package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class DotProductPipelineNodeConfiguration extends NodeConfigurationImpl {
    public DotProductPipelineNodeConfiguration() {
        super("DotProduct", "Dot product", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(PipelineFieldType.Float, "a", "b"),
                        PipelineFieldType.Float));
    }
}
