package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.gempukku.libgdx.graph.util.SimpleLightingRendererConfiguration;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;

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

        UIRenderPipelineConfiguration.registerPreviewConfigurationBuilder(
                LightingRendererConfiguration.class, new Producer<LightingRendererConfiguration>() {
                    @Override
                    public LightingRendererConfiguration create() {
                        SimpleLightingRendererConfiguration result = new SimpleLightingRendererConfiguration();

                        Lighting3DEnvironment graphShaderLightingEnvironment = new Lighting3DEnvironment();
                        graphShaderLightingEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
                        PointLight pointLight = new PointLight();
                        pointLight.set(Color.WHITE, 1.8f, 1.8f, 4f, 8f);
                        graphShaderLightingEnvironment.addPointLight(new Point3DLight(pointLight));

                        result.setEnvironment("", graphShaderLightingEnvironment);
                        return result;
                    }
                });

        // Register runtime plugin
        RuntimePluginRegistry.register(Lighting3DPluginRuntimeInitializer.class);

        ShadowTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty shadow shader", assetResolver.resolve("template/shadow/empty-shadow-shader.json")));
    }
}
