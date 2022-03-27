package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.ui.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class LibgdxGraphApplication extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    private LibgdxGraphScreen libgdxGraphScreen;
    private ScreenViewport viewport;
    private float scale = 1f;

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);

        try {
            // Initialize design plugins
            PluginRegistry.initializePlugins();
            // Initialize runtime plugins
            PluginRegistryImpl.initializePlugins();
        } catch (ReflectionException exp) {
            throw new GdxRuntimeException(exp);
        }

        VisUI.load();
        WhitePixel.initializeShared();
        PatternTextures.initializeShared();

        FileChooser.setDefaultPrefsName("com.gempukku.libgdx.graph.ui.filechooser");

        skin = new Skin(VisUI.SkinScale.X1.getSkinFile());
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(scale);
        stage = new Stage(viewport);

        libgdxGraphScreen = new LibgdxGraphScreen(skin);
        stage.addActor(libgdxGraphScreen);
        // Support for switching the UI scale
        stage.addListener(
                new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.F12 && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            scale = (scale == 1f) ? 0.5f : 1f;
                            viewport.setUnitsPerPixel(scale);
                            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                            return true;
                        }
                        return false;
                    }
                });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        stage.act();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        libgdxGraphScreen.dispose();
        skin.dispose();
        stage.dispose();

        PatternTextures.disposeShared();
        WhitePixel.disposeShared();
        VisUI.dispose();

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }
}
