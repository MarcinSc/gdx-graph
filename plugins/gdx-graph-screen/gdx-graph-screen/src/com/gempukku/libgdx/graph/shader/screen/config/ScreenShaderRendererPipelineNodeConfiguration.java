package com.gempukku.libgdx.graph.shader.screen.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class ScreenShaderRendererPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ScreenShaderRendererPipelineNodeConfiguration() {
        super("FullScreenShaderRenderer", "Full Screen Shader", "Shaders");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("camera", "Camera", false, Camera));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, GraphNodeInputSide.Top, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", GraphNodeOutputSide.Bottom, RenderPipeline));
    }
}
