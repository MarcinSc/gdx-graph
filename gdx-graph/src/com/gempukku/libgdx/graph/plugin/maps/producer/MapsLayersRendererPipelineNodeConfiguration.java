package com.gempukku.libgdx.graph.plugin.maps.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class MapsLayersRendererPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public MapsLayersRendererPipelineNodeConfiguration() {
        super("MapRendererLayer", "Map layer renderer", "Maps");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("camera", "Camera", true, Camera));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", true, RenderPipeline));
    }
}
