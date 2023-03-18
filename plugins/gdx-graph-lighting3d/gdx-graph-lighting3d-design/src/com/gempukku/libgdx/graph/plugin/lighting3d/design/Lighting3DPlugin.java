package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class Lighting3DPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-lighting3d";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(1, 0, 0);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(GdxGraphApplication gdxGraphApplication) {
        Lighting3DPluginRuntimeInitializer.register();

        UICommonShaderConfiguration.register(new PhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new BlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowBlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new GraphBoxProducerImpl(new ApplyNormalMapShaderNodeConfiguration()));
        UICommonShaderConfiguration.register(new AmbientLightBoxProducer());
        UICommonShaderConfiguration.register(new DirectionalLightBoxProducer());
        UICommonShaderConfiguration.register(new PointLightBoxProducer());
        UICommonShaderConfiguration.register(new SpotlightBoxProducer());

        UIPipelineConfiguration.register(new ShadowShaderRendererBoxProducer());
    }

    @Override
    public void deregisterPlugin() {

    }
}
