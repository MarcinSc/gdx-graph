package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.gdx.plugins.jar.JarsPluginsProvider;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.BoneAnimationPlugin;
import com.gempukku.libgdx.graph.plugin.callback.design.RenderCallbackPlugin;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.Lighting3DPlugin;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsPlugin;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesPlugin;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenPlugin;
import com.gempukku.libgdx.graph.ui.LibgdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.plugin.JsonGdxGraphPlugin;

import java.io.File;
import java.io.IOException;

public class GraphEditorLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1440;
        config.height = 810;

        JarsPluginsProvider<GdxGraphApplication, GdxGraphPlugin> jarsPluginsProvider;
        try {
            jarsPluginsProvider = new JarsPluginsProvider<>();
            jarsPluginsProvider.initializePluginsAndClassloader(new File("plugins"), "Gdx-Graph-Plugin");
        } catch (Exception exp) {
            throw new RuntimeException("Unable to initialize application, due to plugin problems", exp);
        }

        AppendPluginsProvider pluginsProvider = new AppendPluginsProvider(jarsPluginsProvider);
        pluginsProvider.appendPlugin(new RenderCallbackPlugin());
        pluginsProvider.appendPlugin(new JsonGdxGraphPlugin("gdx-graph-ui", "config/plugin-ui-config.json"));
        pluginsProvider.appendPlugin(new ScreenPlugin());
        pluginsProvider.appendPlugin(new ParticlesPlugin());
        pluginsProvider.appendPlugin(new ModelsPlugin());
        pluginsProvider.appendPlugin(new BoneAnimationPlugin());
        pluginsProvider.appendPlugin(new Lighting3DPlugin());
        pluginsProvider.appendPlugin(new JsonGdxGraphPlugin("gdx-graph-maps", "config/plugin-maps-config.json"));

        new LwjglApplication(new LibgdxGraphApplication(pluginsProvider), config);
    }
}
