package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class GammaCorrectionPipelineNodeConfiguration extends NodeConfigurationImpl {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection", "Gamma correction", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("gamma", "Gamma", PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, PipelineFieldType.RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, PipelineFieldType.RenderPipeline));
    }
}
