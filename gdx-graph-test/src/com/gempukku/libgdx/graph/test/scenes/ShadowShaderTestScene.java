package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallback;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPublicData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.CommonPropertiesModelInstanceModelAdapter;

public class ShadowShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;
    private final Array<Disposable> disposables = new Array<>();
    private float cameraPositionAngle = MathUtils.PI * 3f / 4f;
    private Stage stage;
    private Lighting3DEnvironment environment;
    private ModelInstance sphereInstance;

    @Override
    public void initializeScene() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100;
        updateCamera();

        pipelineRenderer = loadPipelineRenderer();

        float halfSize = 5;

        ModelBuilder modelBuilder = new ModelBuilder();
        Model wall = modelBuilder.createBox(halfSize * 2, halfSize * 2, 0.001f, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Model sphere = modelBuilder.createSphere(2, 2, 2, 50, 50, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        disposables.add(wall);
        disposables.add(sphere);

        ModelInstance wallInstance = new ModelInstance(wall);
        wallInstance.transform.translate(0, 0, -3);
        sphereInstance = new ModelInstance(sphere);

        GraphModels graphModels = pipelineRenderer.getPluginData(GraphModels.class);

        CommonPropertiesModelInstanceModelAdapter wallAdapter = new CommonPropertiesModelInstanceModelAdapter(wallInstance, graphModels);
        wallAdapter.getPropertyContainer().setValue("Color", Color.LIGHT_GRAY);
        CommonPropertiesModelInstanceModelAdapter sphereAdapter = new CommonPropertiesModelInstanceModelAdapter(sphereInstance, graphModels);
        sphereAdapter.getPropertyContainer().setValue("Color", Color.RED);

        wallAdapter.addTag("Color");
        wallAdapter.addTag("Color Shadow");
        sphereAdapter.addTag("Color");
        sphereAdapter.addTag("Color Shadow");

        Lighting3DPublicData lighting = pipelineRenderer.getPluginData(Lighting3DPublicData.class);
        environment = new Lighting3DEnvironment(new Vector3(), 20f);
        environment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        Directional3DLight directionalLight = new Directional3DLight();
        directionalLight.setColor(Color.WHITE);
        directionalLight.setIntensity(0.4f);
        directionalLight.setDirection(0, 0, -1);
        directionalLight.setShadowsEnabled(true);
        directionalLight.setShadowBufferSize(512);
        environment.addDirectionalLight(directionalLight);
        lighting.setEnvironment("Scene", environment);

        stage = createStage();
        disposables.add(stage);

        Gdx.input.setInputProcessor(stage);

        UIPluginPublicData ui = pipelineRenderer.getPluginData(UIPluginPublicData.class);
        ui.setStage("Stage", stage);
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        final Slider positionAngle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        positionAngle.setValue(cameraPositionAngle);
        positionAngle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraPositionAngle = positionAngle.getValue();
                        updateCamera();
                    }
                });
        tbl.add(positionAngle).growX().row();
        final Slider ballPosition = new Slider(0, 6.0f, 0.001f, false, skin);
        ballPosition.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereInstance.transform.setToTranslation(0, 0, -ballPosition.getValue());
                    }
                });
        tbl.add(ballPosition).growX().row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        float cameraDistance = 10f;

        camera.position.set(-cameraDistance * MathUtils.sin(cameraPositionAngle), 0, -cameraDistance * MathUtils.cos(cameraPositionAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0f, 0f, 0f);
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);
        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("test/shadow-shader-test.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(RenderCallbackPublicData.class).setRenderCallback(
                "Callback", new RenderCallback() {
                    @Override
                    public void renderCallback(RenderPipeline renderPipeline, PipelineDataProvider pipelineDataProvider, PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback) {
                        RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                        Directional3DLight firstLight = environment.getDirectionalLights().get(0);
                        RenderPipelineBuffer shadowFrameBuffer = firstLight.getShadowFrameBuffer();
                        renderPipeline.drawTexture(shadowFrameBuffer, currentBuffer, pipelineRenderingContext, pipelineDataProvider.getFullScreenRender());
                    }
                });
        return pipelineRenderer;
    }
}