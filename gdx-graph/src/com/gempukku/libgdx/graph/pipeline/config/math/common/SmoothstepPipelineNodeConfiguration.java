package com.gempukku.libgdx.graph.pipeline.config.math.common;

import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class SmoothstepPipelineNodeConfiguration extends NodeConfigurationImpl {
    public SmoothstepPipelineNodeConfiguration() {
        super("Smoothstep", "Smoothstep", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl("edge0", "Edge 0", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("edge1", "Edge 1", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new MathCommonOutputTypeFunction(PipelineFieldType.Float, new String[]{"input"}, new String[]{"edge0", "edge1"}),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
