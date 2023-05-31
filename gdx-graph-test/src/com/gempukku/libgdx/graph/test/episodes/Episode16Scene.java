package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.impl.SimplePipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.pipeline.util.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleLightingRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;
import com.gempukku.libgdx.graph.util.model.PropertiesRenderableModel;

public class Episode16Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model shipModel;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Lighting3DEnvironment lights;
    private final float cameraAngle = -0.5f;
    private final float cameraDistance = 1.3f;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private PropertiesRenderableModel screenModel;
    private MapWritablePropertyContainer screenModelPropertyContainer;
    private SimplePipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "YouTube Episode 16";
    }

    @Override
    public void initializeScene() {
        lights = createLights();
        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(shaderConfiguration);

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
        camera.far = 5;
        return camera;
    }

    private void createModels(ModelContainer<RenderableModel> modelContainer) {
        JsonReader jsonReader = new JsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        shipModel = modelLoader.loadModel(Gdx.files.classpath("model/luminaris/luminaris.g3dj"));

        ModelInstance modelInstance = new ModelInstance(shipModel);
        final float scale = 0.025f;
        modelInstance.transform.idt().translate(-0.3f, 0.11f, 0).scale(scale, scale, scale);

        MaterialModelInstanceModelAdapter modelAdapter = new MaterialModelInstanceModelAdapter(modelInstance, modelContainer);
        modelAdapter.addTag("Default");

        ObjectMap<String, BasicShader.Attribute> attributes = shaderConfiguration.getShaderAttributes("CRT Monitor");

        VertexAttributes vertexAttributes = GraphModelUtil.getVertexAttributes(attributes);
        ObjectMap<VertexAttribute, ShaderPropertySource> propertySourceMap = GraphModelUtil.getPropertySourceMap(shaderConfiguration, "CRT Monitor", vertexAttributes);

        Model model = new ModelBuilder().createRect(
                -1f, 1f, 0,
                -1f, -1f, 0,
                1f, -1f, 0,
                1f, 1f, 0,
                0, 0, 1,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        try {
            Mesh mesh = model.meshes.get(0);
            short[] indices = new short[mesh.getNumIndices()];
            mesh.getIndices(indices);
            screenModelPropertyContainer = new MapWritablePropertyContainer();
            screenModelPropertyContainer.setValue("Position", createVector3Value(VertexAttributes.Usage.Position, mesh));
            screenModel = new PropertiesRenderableModel(vertexAttributes, propertySourceMap,
                    mesh.getNumVertices(), indices, screenModelPropertyContainer);

            modelContainer.addModel(screenModel);
        } finally {
            model.dispose();
        }
    }

    private static ArrayValuePerVertex<Vector3> createVector3Value(int usage, Mesh mesh) {
        int vertexCount = mesh.getNumVertices();
        int floatsPerVertex = mesh.getVertexSize() / 4;

        float[] vertices = new float[vertexCount * floatsPerVertex];
        mesh.getVertices(vertices);

        VertexAttribute attribute = mesh.getVertexAttributes().findByUsage(usage);
        if (attribute == null)
            return null;

        int offset = attribute.offset / 4;

        Array<Vector3> result = new Array<>(Vector3.class);
        for (int i = 0; i < vertexCount; i++) {
            result.add(new Vector3(
                    vertices[floatsPerVertex * i + offset + 0],
                    vertices[floatsPerVertex * i + offset + 1],
                    vertices[floatsPerVertex * i + offset + 2]));
        }

        return new ArrayValuePerVertex<>(result.toArray());
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        final CheckBox distort = new CheckBox("Distort", skin);
        distort.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        configuration.getPipelinePropertyContainer().setValue("Distort", distort.isChecked());
                        if (distort.isChecked()) {
                            screenModel.addTag("CRT Monitor");
                        } else {
                            screenModel.removeTag("CRT Monitor");
                        }
                    }
                });

        final Slider distortion = new Slider(0f, 0.3f, 0.001f, false, skin);
        distortion.setValue(0.1f);
        distortion.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        screenModelPropertyContainer.setValue("Distortion", distortion.getValue());
                    }
                });

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);
        tbl.add(distort).left().colspan(2).row();
        tbl.add("Distortion");
        tbl.add(distortion).width(300).row();

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

        float y = 0.3f;
        camera.position.set(cameraDistance * MathUtils.sin(cameraAngle), y, cameraDistance * MathUtils.cos(cameraAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, y - 0.3f, 0f);
        camera.update();

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        shipModel.dispose();
        screenModel.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        configuration.dispose();
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

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode16.json"), configuration);
    }
}