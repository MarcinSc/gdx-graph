package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class RenderSizePipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public RenderSizePipelineNodeConfiguration() {
        super("RenderSize", "Render size", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("size", "Size", Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("width", "Width", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("height", "Height", Float));
    }
}
