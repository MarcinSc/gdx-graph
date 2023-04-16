package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class GammaCorrectionPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection", "Gamma correction", "Post-processing");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("gamma", "Gamma", PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, true, PipelineFieldType.RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, PipelineFieldType.RenderPipeline));
    }
}
