package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.particles.design.generator.PositionParticleGeneratorProducer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndParticlesShaderEditorProducer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticlesShaderRendererEditorProducer;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModelProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.gempukku.libgdx.graph.util.particles.generator.LinePositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PointPositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SpherePositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SphereSurfacePositionGenerator;
import com.kotcrab.vis.ui.VisUI;

public class UIParticlesPlugin implements UIGdxGraphPlugin {
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIParticleEffectGraphType graphType = new UIParticleEffectGraphType(VisUI.getSkin().getDrawable("graph-particle-effect-icon"));
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UIParticlesShaderConfiguration.register(new EndParticlesShaderEditorProducer());
        UIParticlesShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifetimeShaderNodeConfiguration()));
        UIParticlesShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifePercentageShaderNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new ParticlesShaderRendererEditorProducer());

        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Float, ParticleAttributeFunctions.ParticleBirth);
        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Float, ParticleAttributeFunctions.ParticleDeath);

        UIModelShaderConfiguration.registerPreviewModel(
                "Sprite particles", new Producer<PreviewRenderableModelProducer>() {
                    @Override
                    public PreviewRenderableModelProducer create() {
                        return new ParticlePreviewRenderableModelProducer();
                    }
                });

        UIParticlesShaderConfiguration.registerParticleGeneratorProducer(
                "Point", new PositionParticleGeneratorProducer(new PointPositionGenerator()));
        UIParticlesShaderConfiguration.registerParticleGeneratorProducer(
                "Sphere", new PositionParticleGeneratorProducer(new SpherePositionGenerator(0.4f)));
        UIParticlesShaderConfiguration.registerParticleGeneratorProducer(
                "Sphere surface", new PositionParticleGeneratorProducer(new SphereSurfacePositionGenerator(0.4f)));

        LinePositionGenerator linePositionGenerator = new LinePositionGenerator();
        linePositionGenerator.getPoint1().set(-0.4f, 0, 0);
        linePositionGenerator.getPoint2().set(0.4f, 0, 0);
        UIParticlesShaderConfiguration.registerParticleGeneratorProducer(
                "Line", new PositionParticleGeneratorProducer(linePositionGenerator));

        // Register runtime plugin
        RuntimePluginRegistry.register(ParticlesPluginRuntimeInitializer.class);

        // Register shader templates
        ParticlesTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty billboard", assetResolver.resolve("template/particles/empty-particles-shader.json")));
    }
}
