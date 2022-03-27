package com.gempukku.libgdx.graph.pipeline.config.math.common;

import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class LerpPipelineNodeConfiguration extends NodeConfigurationImpl {
    public LerpPipelineNodeConfiguration() {
        super("Mix", "Mix (lerp)", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("t", "T", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new MathCommonOutputTypeFunction(PipelineFieldType.Float, new String[]{"a", "b"}, new String[]{"t"}),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
