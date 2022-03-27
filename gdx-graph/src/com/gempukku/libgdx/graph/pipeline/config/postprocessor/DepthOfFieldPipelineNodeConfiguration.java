package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class DepthOfFieldPipelineNodeConfiguration extends NodeConfigurationImpl {
    public DepthOfFieldPipelineNodeConfiguration() {
        super("DepthOfField", "Depth of Field", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl("focusDistance", "Focus Distance Range", true, Vector2));
        addNodeInput(
                new GraphNodeInputImpl("nearDistanceBlur", "Near Distance Blur", Float));
        addNodeInput(
                new GraphNodeInputImpl("farDistanceBlur", "Far Distance Blur", Float));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
