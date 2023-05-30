package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.particles.ParticleModel;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PositionPropertyGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PropertyGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SpherePositionGenerator;
import com.gempukku.libgdx.graph.util.sprite.SpriteUtil;

public class ParticlesShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;
    private ParticleModel particleModel;
    private PipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "Particles Test";
    }

    @Override
    public void initializeScene() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100;
        camera.position.set(0, 0, 3);
        camera.up.set(0, 1, 0);
        camera.lookAt(0, 0, 0);
        camera.update();

        pipelineRenderer = loadPipelineRenderer();

        shaderConfiguration.getGlobalUniforms("Test").setValue("Color", Color.RED);

        particleModel = new ParticleModel(1000, shaderConfiguration, shaderConfiguration, "Test");
        particleModel.addParticleBirthProperty("Particle Birth");
        particleModel.addParticleDeathProperty("Particle Death");

        createEffect(particleModel, new Vector3(0, 0, 0));
        createEffect(particleModel, new Vector3(2, 0, 0));
    }

    private void createEffect(ParticleModel particleModel, Vector3 center) {
        SpherePositionGenerator positionGenerator = new SpherePositionGenerator();
        positionGenerator.getCenter().set(center);
        positionGenerator.setRadius(0.3f);
        DefaultParticleGenerator particleGenerator = new DefaultParticleGenerator(1f, 0, 10);
        particleGenerator.setPropertyGenerator("Position", new PositionPropertyGenerator(positionGenerator));
        particleGenerator.setPropertyGenerator("UV",
                new PropertyGenerator() {
                    @Override
                    public Object generateProperty(float seed) {
                        return SpriteUtil.QUAD_UVS;
                    }
                });
        particleModel.addGenerator(timeKeeper.getTime(), particleGenerator);
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        particleModel.update(timeKeeper.getTime());

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        particleModel.dispose();
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        configuration = new PipelineRendererConfiguration(timeKeeper);
        configuration.getPipelinePropertyContainer().setValue("Camera", camera);

        shaderConfiguration = new SimpleShaderRendererConfiguration(configuration.getPipelinePropertyContainer());
        configuration.setConfig(ShaderRendererConfiguration.class, shaderConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/particles-shader-test.json"), configuration);
    }
}