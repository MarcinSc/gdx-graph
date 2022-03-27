package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class PipelineRendererNodeConfiguration extends NodeConfigurationImpl {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl("pipeline", "Pipeline", true, RenderPipeline));
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, Vector2));
        addNodeInput(
                new GraphNodeInputImpl("size", "Size", false, Vector2));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
