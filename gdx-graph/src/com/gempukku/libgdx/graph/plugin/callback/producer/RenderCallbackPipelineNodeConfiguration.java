package com.gempukku.libgdx.graph.plugin.callback.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class RenderCallbackPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public RenderCallbackPipelineNodeConfiguration() {
        super("RenderCallback", "Render callback", "Pipeline");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, RenderPipeline));
    }
}
