package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class BloomPipelineNodeConfiguration extends NodeConfigurationImpl {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("bloomRadius", "Radius", Float));
        addNodeInput(
                new GraphNodeInputImpl("minimalBrightness", "Min Brightness", Float));
        addNodeInput(
                new GraphNodeInputImpl("bloomStrength", "Strength", Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
