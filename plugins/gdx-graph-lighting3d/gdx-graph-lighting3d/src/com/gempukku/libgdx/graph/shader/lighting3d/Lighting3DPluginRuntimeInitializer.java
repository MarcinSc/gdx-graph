package com.gempukku.libgdx.graph.shader.lighting3d;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.depth.DepthShaderGraphType;
import com.gempukku.libgdx.graph.shader.lighting3d.producer.*;

public class Lighting3DPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        GraphTypeRegistry.registerType(new DepthShaderGraphType());

        ModelShaderConfiguration.addNodeBuilder(new EndShadowShaderNodeBuilder());

        RendererPipelineConfiguration.register(new ShadowShaderRendererPipelineNodeProducer());

        ModelShaderConfiguration.addNodeBuilder(new BlinnPhongLightingShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PhongLightingShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ShadowPhongLightingShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ShadowBlinnPhongLightingShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ApplyNormalMapShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new AmbientLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DirectionalLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PointLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SpotLightShaderNodeBuilder());
    }

    @Override
    public void dispose() {

    }
}
