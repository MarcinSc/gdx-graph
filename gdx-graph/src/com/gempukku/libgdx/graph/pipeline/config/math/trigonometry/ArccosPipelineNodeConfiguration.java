package com.gempukku.libgdx.graph.pipeline.config.math.trigonometry;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class ArccosPipelineNodeConfiguration extends NodeConfigurationImpl {
    public ArccosPipelineNodeConfiguration() {
        super("Arccos", "Arccosine", "Math/Trigonometry");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new SameTypeOutputTypeFunction("input"),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3));
    }
}
