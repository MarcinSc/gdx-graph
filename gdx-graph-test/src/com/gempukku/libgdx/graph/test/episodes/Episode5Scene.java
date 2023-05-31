package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
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
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

public class Episode5Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model sphereModel;
    private Camera camera;
    private Texture rockTexture;
    private Stage stage;
    private Skin skin;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private MaterialModelInstanceModelAdapter sphereAdapter;
    private SimplePipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "YouTube Episode 5";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        rockTexture = new Texture(Gdx.files.classpath("image/seamless_rock_face_texture_by_hhh316.jpg"));
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(rockTexture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = createStage();

        camera = createCamera();
        pipelineRenderer = loadPipelineRenderer();
        createModels(shaderConfiguration);

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        camera.position.set(1f, 0.5f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
        return camera;
    }

    private void createModels(ModelContainer<RenderableModel> modelContainer) {
        sphereAdapter = new MaterialModelInstanceModelAdapter(new ModelInstance(sphereModel), modelContainer);
        sphereAdapter.addTag("Cover");
    }

    private Stage createStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider amount = new Slider(0, 10, 0.1f, false, skin);
        amount.setValue(1f);
        amount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereAdapter.setProperty("Amount", amount.getValue());
                    }
                });
        final Slider scale = new Slider(1, 50, 1f, false, skin);
        scale.setValue(20f);
        scale.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereAdapter.setProperty("Scale", scale.getValue());
                    }
                });
        final Slider x = new Slider(-1, 1, 0.01f, false, skin);
        x.setValue(0f);
        final Slider y = new Slider(-1, 1, 0.01f, false, skin);
        y.setValue(1f);
        final Slider z = new Slider(-1, 1, 0.01f, false, skin);
        z.setValue(0f);
        ChangeListener directionListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sphereAdapter.setProperty("Direction", new Vector3(x.getValue(), y.getValue(), z.getValue()));
            }
        };
        x.addListener(directionListener);
        y.addListener(directionListener);
        z.addListener(directionListener);


        Table tbl = new Table();
        tbl.add(new Label("Cover amount", skin));
        tbl.add(amount).row();
        tbl.add(new Label("Cover scale", skin));
        tbl.add(scale).row();
        tbl.add(new Label("X", skin));
        tbl.add(x).row();
        tbl.add(new Label("Y", skin));
        tbl.add(y).row();
        tbl.add(new Label("Z", skin));
        tbl.add(z).row();

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
        skin.dispose();
        stage.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        configuration.dispose();
        rockTexture.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        configuration = new SimplePipelineRendererConfiguration(timeKeeper);
        configuration.getPipelinePropertyContainer().setValue("Camera", camera);

        SimpleUIRendererConfiguration uiConfiguration = new SimpleUIRendererConfiguration();
        uiConfiguration.setStage("", stage);
        configuration.setConfig(UIRendererConfiguration.class, uiConfiguration);

        shaderConfiguration = new SimpleShaderRendererConfiguration(configuration.getPipelinePropertyContainer());
        configuration.setConfig(ShaderRendererConfiguration.class, shaderConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode5.json"), configuration);
    }
}