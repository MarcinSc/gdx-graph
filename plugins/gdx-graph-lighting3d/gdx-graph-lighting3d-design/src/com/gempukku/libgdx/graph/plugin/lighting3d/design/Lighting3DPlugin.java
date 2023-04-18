package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class Lighting3DPlugin {
    public void initialize() {
        GraphTypeRegistry.registerType(new UIShadowShaderGraphType());

        Lighting3DPluginRuntimeInitializer.register();

        UICommonShaderConfiguration.register(new PhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new BlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowBlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ApplyNormalMapShaderNodeConfiguration()));
        UICommonShaderConfiguration.register(new AmbientLightBoxProducer());
        UICommonShaderConfiguration.register(new DirectionalLightBoxProducer());
        UICommonShaderConfiguration.register(new PointLightBoxProducer());
        UICommonShaderConfiguration.register(new SpotlightBoxProducer());

        UIPipelineConfiguration.register(new ShadowShaderRendererBoxProducer());
    }
}
