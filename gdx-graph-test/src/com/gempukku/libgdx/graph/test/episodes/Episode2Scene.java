package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallback;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPublicData;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class Episode2Scene implements LibgdxGraphTestScene {
    private Stage stage;
    private Skin skin;

    private PipelineRenderer pipelineRenderer;
    private Model sphereModel;
    private final Array<RenderableProvider> renderableProviders = new Array<>();
    private Environment environment;
    private Camera camera;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelBatch modelBatch;

    @Override
    public String getName() {
        return "YouTube Episode 2";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        constructStage();

        createModels();
        environment = createEnvironment();
        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        modelBatch = new ModelBatch();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(5f, 5f, 5f);
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
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        renderableProviders.add(new ModelInstance(sphereModel));
    }

    private void constructStage() {
        stage = new Stage(new ScreenViewport());

        Label label = new Label("This is example label", skin);
        label.setBounds(0, 0, 200, 20);
        TextButton textButton = new TextButton("Test", skin);
        textButton.setBounds(0, 20, 200, 20);

        stage.addActor(label);
        stage.addActor(textButton);
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

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        renderableProviders.clear();
        modelBatch.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        skin.dispose();
        stage.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode2.json"), timeKeeper);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        pipelineRenderer.getPluginData(RenderCallbackPublicData.class).setRenderCallback(
                "Callback", new RenderCallback() {

                    @Override
                    public void renderCallback(RenderPipeline renderPipeline, PipelineDataProvider pipelineDataProvider, PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback) {
                        RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                        int width = currentBuffer.getWidth();
                        int height = currentBuffer.getHeight();
                        float viewportWidth = camera.viewportWidth;
                        float viewportHeight = camera.viewportHeight;
                        if (width != viewportWidth || height != viewportHeight) {
                            camera.viewportWidth = width;
                            camera.viewportHeight = height;
                            camera.update();
                        }

                        currentBuffer.beginColor();

                        modelBatch.begin(camera);
                        modelBatch.render(renderableProviders, environment);
                        modelBatch.end();

                        currentBuffer.endColor();
                    }
                });
        return pipelineRenderer;
    }
}