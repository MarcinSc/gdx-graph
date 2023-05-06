package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
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

        GraphModels particleEffects = pipelineRenderer.getPluginData(GraphModels.class);
        particleEffects.setGlobalProperty("Test", "Color", Color.RED);

        particleModel = new ParticleModel(1000, particleEffects, "Test");

        createEffect(particleModel, new Vector3(0, 0, 0), 0.1f);
        createEffect(particleModel, new Vector3(2, 0, 0), 0.2f);
    }

    private void createEffect(ParticleModel particleModel, Vector3 center, float size) {
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

        MapWritablePropertyContainer properties = new MapWritablePropertyContainer();
        properties.setValue("Size", size);

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
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/particles-shader-test.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        return pipelineRenderer;
    }
}