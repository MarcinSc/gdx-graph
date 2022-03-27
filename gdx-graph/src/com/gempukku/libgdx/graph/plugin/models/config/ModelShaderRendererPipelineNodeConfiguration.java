package com.gempukku.libgdx.graph.plugin.models.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class ModelShaderRendererPipelineNodeConfiguration extends NodeConfigurationImpl {
    public ModelShaderRendererPipelineNodeConfiguration() {
        super("GraphShaderRenderer", "Model Shaders", "Shaders");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
