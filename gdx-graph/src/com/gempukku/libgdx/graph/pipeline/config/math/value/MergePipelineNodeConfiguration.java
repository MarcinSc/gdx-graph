package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class MergePipelineNodeConfiguration extends NodeConfigurationImpl {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl("x", "X", Float));
        addNodeInput(
                new GraphNodeInputImpl("y", "Y", Float));
        addNodeInput(
                new GraphNodeInputImpl("z", "Z", Float));
        addNodeInput(
                new GraphNodeInputImpl("w", "W", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("v2", "Vector2", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("v3", "Vector3", Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", Color));
    }
}
