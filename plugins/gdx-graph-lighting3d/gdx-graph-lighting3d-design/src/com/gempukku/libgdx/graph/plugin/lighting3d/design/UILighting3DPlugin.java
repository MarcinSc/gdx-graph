package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UILighting3DPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIShadowShaderGraphType graphType = new UIShadowShaderGraphType();
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UICommonShaderConfiguration.register(new EndShadowShaderEditorProducer());

        UICommonShaderConfiguration.register(new PhongLightingEditorProducer());
        UICommonShaderConfiguration.register(new ShadowPhongLightingEditorProducer());
        UICommonShaderConfiguration.register(new BlinnPhongLightingEditorProducer());
        UICommonShaderConfiguration.register(new ShadowBlinnPhongLightingEditorProducer());
        UICommonShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ApplyNormalMapShaderNodeConfiguration()));
        UICommonShaderConfiguration.register(new AmbientLightEditorProducer());
        UICommonShaderConfiguration.register(new DirectionalLightEditorProducer());
        UICommonShaderConfiguration.register(new PointLightEditorProducer());
        UICommonShaderConfiguration.register(new SpotlightEditorProducer());

        UIRenderPipelineConfiguration.register(new ShadowShaderRendererEditorProducer());

        // Register runtime plugin
        Lighting3DPluginRuntimeInitializer.register();

        ShadowTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty shadow shader", assetResolver.resolve("template/shadow/empty-shadow-shader.json")));
    }
}
