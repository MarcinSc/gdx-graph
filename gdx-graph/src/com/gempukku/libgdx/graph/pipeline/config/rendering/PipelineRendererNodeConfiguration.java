package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class PipelineRendererNodeConfiguration extends DefaultMenuNodeConfiguration {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer", "Pipeline");
        addNodeInput(
                new DefaultGraphNodeInput("pipeline", "Pipeline", true, RenderPipeline));
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", false, Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, RenderPipeline));
    }
}
