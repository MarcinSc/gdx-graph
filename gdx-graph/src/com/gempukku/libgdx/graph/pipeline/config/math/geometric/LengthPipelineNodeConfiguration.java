package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class LengthPipelineNodeConfiguration extends NodeConfigurationImpl {
    public LengthPipelineNodeConfiguration() {
        super("Length", "Length", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", PipelineFieldType.Float));
    }
}
