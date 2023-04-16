package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.plugin.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.test.episodes.*;
import com.gempukku.libgdx.graph.test.scenes.*;

public class ReloadableGraphTestApplication extends ApplicationAdapter {
    private LibgdxGraphTestScene[] scenes;
    private int loadedIndex;
    private int width;
    private int height;
    private final FPSLogger fpsLogger = new FPSLogger();

    private boolean profile = false;
    private GLProfiler profiler;
    private Skin uiSkin;
    private Stage stage;
    private Label sceneLabel;
    private Label profilingLabel;

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
        RenderCallbackPluginRuntimeInitializer.register();
        UIPluginRuntimeInitializer.register();
        ParticlesPluginRuntimeInitializer.register();
        ScreenPluginRuntimeInitializer.register();
        ModelsPluginRuntimeInitializer.register();
        BoneAnimationPluginRuntimeInitializer.register();
        Lighting3DPluginRuntimeInitializer.register(
                1, 0, 0,
                0.2f, 5);

        scenes = new LibgdxGraphTestScene[]{
                new Episode1Scene(),
                new Episode2Scene(),
                new Episode3Scene(),
                new Episode4Scene(),
                new Episode5Scene(),
                new Episode6Scene(),
                new Episode7Scene(),
                new Episode8Scene(),
                new Episode9Scene(),
                new Episode11Scene(),
                new Episode12Scene(),
                new Episode13Scene(),
                new Episode14Scene(),
                new Episode15Scene(),
                new Episode16Scene(),
                new Episode17Scene(),
                new Episode18Scene(),
                new Episode19Scene(),
                new Episode20Scene(),
                new Episode21Scene(),
                new Episode22Scene(),
                new Episode23Scene(),
                new Episode24Scene(),
                new SpriteShaderTestScene(),
                new ParticlesShaderTestScene(),
                new ShadowShaderTestScene(),
                new SDFTextShaderTestScene(),
                new HierarchyAndTransformTestScene(),
                new StylizedShadingShaderTestScene(),
                new TopDownCameraTestScene()
        };
        loadedIndex = scenes.length - 1;

        scenes[loadedIndex].initializeScene();

        uiSkin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        sceneLabel = new Label("", uiSkin);
        profilingLabel = new Label("", uiSkin);

        Table tbl1 = new Table(uiSkin);

        tbl1.setFillParent(true);
        tbl1.align(Align.bottomRight);

        tbl1.add(sceneLabel).width(250).pad(10f);
        tbl1.row();

        Table tbl2 = new Table(uiSkin);
        tbl2.setFillParent(true);
        tbl2.align(Align.topRight);
        tbl2.add(profilingLabel).width(200).pad(10f);
        tbl2.row();

        stage.addActor(tbl1);
        stage.addActor(tbl2);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        stage.getViewport().update(width, height, true);
        scenes[loadedIndex].resizeScene(width, height);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            scenes[loadedIndex].disposeScene();
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P) && loadedIndex > 0) {
            scenes[loadedIndex].disposeScene();
            loadedIndex--;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && loadedIndex < scenes.length - 1) {
            scenes[loadedIndex].disposeScene();
            loadedIndex++;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            if (profile)
                disableProfiler();
            else
                enableProfiler();
        }

        long start = 0;
        if (profile) {
            fpsLogger.log();
            profiler.reset();
            start = System.nanoTime();
        }

        scenes[loadedIndex].renderScene();

        StringBuilder sb = new StringBuilder();
        if (profile) {
            float ms = (System.nanoTime() - start) / 1000000f;

            sb.append("Time: " + SimpleNumberFormatter.format(ms) + "ms\n");
            sb.append("GL Calls: " + profiler.getCalls() + "\n");
            sb.append("Draw calls: " + profiler.getDrawCalls() + "\n");
            sb.append("Shader switches: " + profiler.getShaderSwitches() + "\n");
            sb.append("Texture bindings: " + profiler.getTextureBindings() + "\n");
            sb.append("Vertex calls: " + profiler.getVertexCount().total + "\n");
            long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
            sb.append("Used memory: " + memory + "MB\n");
        }
        profilingLabel.setText(sb.toString());

        sb.setLength(0);
        sb.append("Scene:\n");
        sb.append(scenes[loadedIndex].getName() + "\n");
        sb.append("P - for previous scene\n");
        sb.append("N - for next scene\n");
        sb.append("O - to toggle profiling\n");
        sceneLabel.setText(sb.toString());

        stage.draw();
    }

    @Override
    public void dispose() {
        scenes[loadedIndex].disposeScene();

        uiSkin.dispose();
        stage.dispose();

        System.out.println("Exit status:");
        System.out.println(Cubemap.getManagedStatus());
        System.out.println(GLFrameBuffer.getManagedStatus());
        System.out.println(Mesh.getManagedStatus());
        System.out.println(Texture.getManagedStatus());
        System.out.println(TextureArray.getManagedStatus());
        System.out.println(ShaderProgram.getManagedStatus());
    }

    private void enableProfiler() {
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        profile = true;
    }

    private void disableProfiler() {
        profiler.disable();

        profile = false;
    }
}
