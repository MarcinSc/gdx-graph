package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class SplitPipelineNodeConfiguration extends NodeConfigurationImpl {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, Color, Vector3, Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("x", "X", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("y", "Y", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("z", "Z", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("w", "W", Float));
    }
}
