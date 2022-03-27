package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class StartPipelineNodeConfiguration extends NodeConfigurationImpl {
    public StartPipelineNodeConfiguration() {
        super("PipelineStart", "Pipeline start", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl("background", "Background color", Color));
        addNodeInput(
                new GraphNodeInputImpl("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
