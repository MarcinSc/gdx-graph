package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class RenderSizePipelineNodeConfiguration extends NodeConfigurationImpl {
    public RenderSizePipelineNodeConfiguration() {
        super("RenderSize", "Render size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("width", "Width", Float));
        addNodeOutput(
                new GraphNodeOutputImpl("height", "Height", Float));
    }
}
