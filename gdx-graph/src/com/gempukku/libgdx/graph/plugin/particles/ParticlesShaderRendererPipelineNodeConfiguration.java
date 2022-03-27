package com.gempukku.libgdx.graph.plugin.particles;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class ParticlesShaderRendererPipelineNodeConfiguration extends NodeConfigurationImpl {
    public ParticlesShaderRendererPipelineNodeConfiguration() {
        super("ParticlesShaderRenderer", "Particle Shaders", "Shaders");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl("camera", "Camera", false, Camera));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
