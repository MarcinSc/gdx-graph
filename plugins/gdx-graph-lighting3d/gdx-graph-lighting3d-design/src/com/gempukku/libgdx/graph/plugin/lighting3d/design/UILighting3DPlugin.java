package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UILighting3DPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIDepthShaderGraphType graphType = new UIDepthShaderGraphType();
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UIShadowShaderConfiguration.register(new EndShadowShaderEditorProducer());

        UIModelShaderConfiguration.register(new PhongLightingEditorProducer());
        UIModelShaderConfiguration.register(new ShadowPhongLightingEditorProducer());
        UIModelShaderConfiguration.register(new BlinnPhongLightingEditorProducer());
        UIModelShaderConfiguration.register(new ShadowBlinnPhongLightingEditorProducer());
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ApplyNormalMapShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new AmbientLightEditorProducer());
        UIModelShaderConfiguration.register(new DirectionalLightEditorProducer());
        UIModelShaderConfiguration.register(new PointLightEditorProducer());
        UIModelShaderConfiguration.register(new SpotlightEditorProducer());

        UIRenderPipelineConfiguration.register(new ShadowShaderRendererEditorProducer());

        // Register runtime plugin
        Lighting3DPluginRuntimeInitializer.register();

        ShadowTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty shadow shader", assetResolver.resolve("template/shadow/empty-shadow-shader.json")));
    }
}
