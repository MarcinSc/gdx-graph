package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class GaussianBlurPipelineNodeConfiguration extends NodeConfigurationImpl {
    public GaussianBlurPipelineNodeConfiguration() {
        super("GaussianBlur", "Gaussian blur", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("blurRadius", "Radius", PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, PipelineFieldType.RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, PipelineFieldType.RenderPipeline));
    }
}
