package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UILighting3DPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register graph type
        GraphTypeRegistry.registerType(new UIShadowShaderGraphType());

        // Register node editors
        UICommonShaderConfiguration.register(new EndShadowShaderBoxProducer());

        UICommonShaderConfiguration.register(new PhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new BlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new ShadowBlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ApplyNormalMapShaderNodeConfiguration()));
        UICommonShaderConfiguration.register(new AmbientLightBoxProducer());
        UICommonShaderConfiguration.register(new DirectionalLightBoxProducer());
        UICommonShaderConfiguration.register(new PointLightBoxProducer());
        UICommonShaderConfiguration.register(new SpotlightBoxProducer());

        UIRenderPipelineConfiguration.register(new ShadowShaderRendererBoxProducer());

        // Register runtime plugin
        Lighting3DPluginRuntimeInitializer.register();
    }
}