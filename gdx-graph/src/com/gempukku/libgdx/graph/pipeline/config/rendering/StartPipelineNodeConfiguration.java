package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class StartPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public StartPipelineNodeConfiguration() {
        super("PipelineStart", "Pipeline start", "Pipeline");
        addNodeInput(
                new DefaultGraphNodeInput("background", "Background color", Color, Vector4));
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, RenderPipeline));
    }
}
