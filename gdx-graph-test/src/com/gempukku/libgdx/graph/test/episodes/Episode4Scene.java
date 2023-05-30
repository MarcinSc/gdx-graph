package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.render.callback.CallbackRendererConfiguration;
import com.gempukku.libgdx.graph.render.callback.RenderCallback;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleCallbackRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;

public class Episode4Scene implements LibgdxGraphTestScene {
    private Stage stage;
    private Skin skin;

    private PipelineRenderer pipelineRenderer;
    private final Array<RenderableProvider> renderableProviders = new Array<>();
    private Model sphereModel;
    private Environment environment;
    private Camera camera1;
    private Camera camera2;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelBatch modelBatch;
    private PipelineRendererConfiguration configuration;

    @Override
    public String getName() {
        return "YouTube Episode 4";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = constructStage();

        createModels();

        environment = createEnvironment();

        camera1 = createCamera();
        camera2 = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        modelBatch = new ModelBatch();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(3f, 3f, 0f);
        camera.lookAt(0, 0, 0);
        camera.update();
        return camera;
    }

    private Environment createEnvironment() {
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        return environment;
    }

    private void createModels() {
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture), ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.5f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        renderableProviders.add(new ModelInstance(sphereModel));
    }

    private Stage constructStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider angle = new Slider(0, 360, 1, false, skin);
        angle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        float angleValue = angle.getValue();
                        float x = 3f * MathUtils.sinDeg(angleValue);
                        float z = 3f * MathUtils.cosDeg(angleValue);

                        camera1.position.set(x, 3f, z);
                        camera1.up.set(0, 1f, 0f);
                        camera1.lookAt(0, 0, 0);
                        camera1.update();
                    }
                });

        Table tbl = new Table();
        tbl.add(new Label("Camera angle", skin));
        tbl.add(angle).row();
        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera1.viewportWidth = width / 2f;
        camera1.viewportHeight = height;
        camera1.update();
        camera2.viewportWidth = width / 2f;
        camera2.viewportHeight = height;
        camera2.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        renderableProviders.clear();
        modelBatch.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        configuration.dispose();
        skin.dispose();
        stage.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        SimpleUIRendererConfiguration uiConfiguration = new SimpleUIRendererConfiguration();
        uiConfiguration.setStage("", stage);

        SimpleCallbackRendererConfiguration callbackConfiguration = new SimpleCallbackRendererConfiguration();
        callbackConfiguration.setRenderCallback("Callback1", new ModelsRenderCallback(camera1));
        callbackConfiguration.setRenderCallback("Callback2", new ModelsRenderCallback(camera2));

        configuration = new PipelineRendererConfiguration(timeKeeper);
        configuration.setConfig(UIRendererConfiguration.class, uiConfiguration);
        configuration.setConfig(CallbackRendererConfiguration.class, callbackConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode4.json"), configuration);
    }

    private class ModelsRenderCallback implements RenderCallback {
        private final Camera camera;

        public ModelsRenderCallback(Camera camera) {
            this.camera = camera;
        }

        @Override
        public void renderCallback(RenderPipeline renderPipeline, PipelineRendererConfiguration pipelineRendererConfiguration, PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback) {
            RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

            OpenGLContext renderContext = pipelineRenderingContext.getRenderContext();
            renderContext.end();
            currentBuffer.beginColor();

            // No idea, why I need to call this
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(camera);
            modelBatch.render(renderableProviders, environment);
            modelBatch.end();

            currentBuffer.endColor();
            renderContext.begin();
        }
    }
}