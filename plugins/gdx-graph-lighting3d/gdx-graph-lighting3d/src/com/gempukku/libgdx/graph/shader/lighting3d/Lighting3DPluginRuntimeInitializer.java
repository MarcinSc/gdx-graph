package com.gempukku.libgdx.graph.shader.lighting3d;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.producer.*;

public class Lighting3DPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private static int maxNumberOfDirectionalLights;
    private static int maxNumberOfPointLights;
    private static int maxNumberOfSpotlights;
    private static float shadowAcneValue;
    private static int shadowSoftness;

    public static void register() {
        register(5, 2, 2, 0.001f, 1);
    }

    public static void register(int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights,
                                float shadowAcneValue, int shadowSoftness) {
        Lighting3DPluginRuntimeInitializer.maxNumberOfDirectionalLights = maxNumberOfDirectionalLights;
        Lighting3DPluginRuntimeInitializer.maxNumberOfPointLights = maxNumberOfPointLights;
        Lighting3DPluginRuntimeInitializer.maxNumberOfSpotlights = maxNumberOfSpotlights;
        Lighting3DPluginRuntimeInitializer.shadowAcneValue = shadowAcneValue;
        Lighting3DPluginRuntimeInitializer.shadowSoftness = shadowSoftness;
        RuntimePluginRegistry.register(Lighting3DPluginRuntimeInitializer.class);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphTypeRegistry.registerType(new ShadowShaderGraphType());

        ModelShaderConfiguration.addNodeBuilder(new EndShadowShaderNodeBuilder());

        RendererPipelineConfiguration.register(new ShadowShaderRendererPipelineNodeProducer(pluginRegistry));

        ModelShaderConfiguration.addNodeBuilder(new BlinnPhongLightingShaderNodeBuilder(maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights));
        ModelShaderConfiguration.addNodeBuilder(new PhongLightingShaderNodeBuilder(maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights));
        ModelShaderConfiguration.addNodeBuilder(new ShadowPhongLightingShaderNodeBuilder(
                maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights,
                shadowAcneValue, shadowSoftness));
        ModelShaderConfiguration.addNodeBuilder(new ShadowBlinnPhongLightingShaderNodeBuilder(
                maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights,
                shadowAcneValue, shadowSoftness));
        ModelShaderConfiguration.addNodeBuilder(new ApplyNormalMapShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new AmbientLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DirectionalLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PointLightShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SpotLightShaderNodeBuilder());

        Lighting3DPrivateData data = new Lighting3DPrivateData();

        pluginRegistry.registerPrivateData(Lighting3DPrivateData.class, data);
        pluginRegistry.registerPublicData(Lighting3DPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}
