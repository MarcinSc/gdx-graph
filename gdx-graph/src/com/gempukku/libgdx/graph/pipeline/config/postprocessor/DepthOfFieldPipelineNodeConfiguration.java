package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class DepthOfFieldPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DepthOfFieldPipelineNodeConfiguration() {
        super("DepthOfField", "Depth of Field", "Post-processing");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("camera", "Camera", true, Camera));
        addNodeInput(
                new DefaultGraphNodeInput("focusDistance", "Focus Distance Range", true, Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("nearDistanceBlur", "Near Distance Blur", Float));
        addNodeInput(
                new DefaultGraphNodeInput("farDistanceBlur", "Far Distance Blur", Float));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, GraphNodeInputSide.Top, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", GraphNodeOutputSide.Bottom, RenderPipeline));
    }
}
