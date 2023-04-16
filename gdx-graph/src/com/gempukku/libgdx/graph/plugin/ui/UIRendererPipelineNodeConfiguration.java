package com.gempukku.libgdx.graph.plugin.ui;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class UIRendererPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer", "Pipeline");
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", false, PipelineFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, RenderPipeline));
    }
}
