package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.impl.SimplePipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleLightingRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

public class Episode13Scene implements LibgdxGraphTestScene {
    private final Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private Lighting3DEnvironment lights;

    private Model tiledWall;
    private Model burner;
    private Model cylinder;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private SimplePipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "YouTube Episode 13";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();
        disposables.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(shaderConfiguration);

        Gdx.input.setInputProcessor(stage);
    }

    private Lighting3DEnvironment createLights() {
        float ambientBrightness = 0.3f;
        float directionalBrightness = 0.8f;
        Lighting3DEnvironment lights = new Lighting3DEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-0.3f, -0.4f, -1);
        lights.addDirectionalLight(new Directional3DLight(directionalLight));
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(15, 4, 0);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 3, 0f);
        camera.update();

        return camera;
    }

    private void createModels(ModelContainer<RenderableModel> modelContainer) {
        ModelBuilder modelBuilder = new ModelBuilder();

        float x = -5f;
        tiledWall = modelBuilder.createRect(
                x, 20, 10,
                x, 0, 10,
                x, 0, -10,
                x, 20, -10,
                1, 0, 0,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        disposables.add(tiledWall);

        float y = 0f;
        burner = modelBuilder.createRect(
                x, y, 5,
                -x, y, 5,
                -x, y, -5,
                x, y, -5,
                0, 1, 0,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        disposables.add(burner);

        float cylinderHeight = 8f;
        float cylinderSize = 7f;
        cylinder = modelBuilder.createCylinder(cylinderSize, cylinderHeight, cylinderSize, 50,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);
        disposables.add(cylinder);

        registerModels(modelContainer);
    }

    private void registerModels(ModelContainer<RenderableModel> modelContainer) {
        float cylinderHeight = 8f;

        ModelInstance wallInstance = new ModelInstance(tiledWall);
        MaterialModelInstanceModelAdapter wallAdapter = new MaterialModelInstanceModelAdapter(wallInstance, modelContainer);
        wallAdapter.addTag("tiled-wall");

        ModelInstance burnerInstance = new ModelInstance(burner);
        MaterialModelInstanceModelAdapter burnerAdapter = new MaterialModelInstanceModelAdapter(burnerInstance, modelContainer);
        burnerAdapter.addTag("burner");

        ModelInstance cylinderInstance = new ModelInstance(cylinder);
        cylinderInstance.transform.idt().translate(0, 0.05f + cylinderHeight / 2f, 0f);
        MaterialModelInstanceModelAdapter cylinderAdapter = new MaterialModelInstanceModelAdapter(cylinderInstance, modelContainer);
        cylinderAdapter.addTag("heat-displacement");
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
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
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
        pipelineRenderer.dispose();
        configuration.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        configuration = new SimplePipelineRendererConfiguration(timeKeeper);
        configuration.getPipelinePropertyContainer().setValue("Camera", camera);

        SimpleUIRendererConfiguration uiConfiguration = new SimpleUIRendererConfiguration();
        uiConfiguration.setStage("", stage);
        configuration.setConfig(UIRendererConfiguration.class, uiConfiguration);

        SimpleLightingRendererConfiguration lightingConfiguration = new SimpleLightingRendererConfiguration();
        configuration.setConfig(LightingRendererConfiguration.class, lightingConfiguration);
        lightingConfiguration.setEnvironment("", lights);

        shaderConfiguration = new SimpleShaderRendererConfiguration(configuration.getPipelinePropertyContainer());
        configuration.setConfig(ShaderRendererConfiguration.class, shaderConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode13.json"), configuration);
    }
}