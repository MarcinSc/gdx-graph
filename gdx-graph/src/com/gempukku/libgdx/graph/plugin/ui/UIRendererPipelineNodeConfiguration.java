package com.gempukku.libgdx.graph.plugin.ui;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class UIRendererPipelineNodeConfiguration extends NodeConfigurationImpl {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
