package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;
import com.gempukku.libgdx.graph.util.particles.ParticleModel;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.LinePositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PositionPropertyGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PropertyGenerator;
import com.gempukku.libgdx.graph.util.sprite.SpriteUtil;

public class Episode18Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Lighting3DEnvironment lights;

    private Model model;
    private final float cameraDistance = 1.5f;
    private float cameraPositionAngle = 0f;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ParticleModel particleModel;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();

        camera = createCamera();
        updateCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getPluginData(GraphModels.class));

        Gdx.input.setInputProcessor(stage);
    }

    private Lighting3DEnvironment createLights() {
        float ambientBrightness = 0.8f;
        float directionalBrightness = 0.8f;
        Lighting3DEnvironment lights = new Lighting3DEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-1f, -0.3f, 0);
        lights.addDirectionalLight(new Directional3DLight(directionalLight));
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100;
        camera.position.set(0, 7, 5);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 4, 0f);
        camera.update();

        return camera;
    }

    private void createModels(GraphModels models) {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.classpath("model/fighter/fighter.g3db"));

        ModelInstance modelInstance = new ModelInstance(model);
        final float scale = 0.0008f;
        modelInstance.transform.idt().scale(scale, scale, scale).rotate(-1, 0, 0f, 90);

        MaterialModelInstanceModelAdapter modelAdapter = new MaterialModelInstanceModelAdapter(modelInstance, models);
        modelAdapter.addTag("Default");

        float height = 0.22f;
        float distance = -1f;
        float min = 0.2f;
        float max = 0.3f;

        particleModel = new ParticleModel(1000, models, "exhaust");

        createExhaust(particleModel, new Vector3(min, height, distance), new Vector3(max, height, distance));
        createExhaust(particleModel, new Vector3(-min, height, distance), new Vector3(-max, height, distance));
        createExhaust(particleModel, new Vector3(min, -height, distance), new Vector3(max, -height, distance));
        createExhaust(particleModel, new Vector3(-min, -height, distance), new Vector3(-max, -height, distance));
    }

    private void createExhaust(ParticleModel model, Vector3 point1, Vector3 point2) {
        final LinePositionGenerator positionGenerator = new LinePositionGenerator();
        positionGenerator.getPoint1().set(point1);
        positionGenerator.getPoint2().set(point2);
        DefaultParticleGenerator particleGenerator = new DefaultParticleGenerator(0.5f, 0, 1000);
        particleGenerator.setPropertyGenerator("Position", new PositionPropertyGenerator(positionGenerator));
        particleGenerator.setPropertyGenerator("UV",
                new PropertyGenerator() {
                    @Override
                    public Object generateProperty(float seed) {
                        return SpriteUtil.QUAD_UVS;
                    }
                });
        PropertyGenerator randomGenerator = new PropertyGenerator() {
            @Override
            public Object generateProperty(float seed) {
                return MathUtils.random(-0.05f, 0.05f);
            }
        };
        particleGenerator.setPropertyGenerator("Move X", randomGenerator);
        particleGenerator.setPropertyGenerator("Move Y", randomGenerator);

        model.addGenerator(0, particleGenerator);
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        Stage stage = new Stage(new ScreenViewport());

        final Slider positionAngle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        positionAngle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraPositionAngle = positionAngle.getValue();
                        updateCamera();
                    }
                });


        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        tbl.add("Camera orbit");
        tbl.add(positionAngle).width(500);
        tbl.row();

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        camera.position.set(cameraDistance * MathUtils.cos(cameraPositionAngle), 1f, cameraDistance * MathUtils.sin(cameraPositionAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0f, 0f, 0f);
        camera.update();
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        stage.act(delta);
        particleModel.update(timeKeeper.getTime());

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        model.dispose();
        particleModel.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode18.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(Lighting3DPublicData.class).setEnvironment("", lights);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        return pipelineRenderer;
    }
}