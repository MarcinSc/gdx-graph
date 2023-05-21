package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.gempukku.gdx.assistant.GdxAssistant;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPlugin;
import com.gempukku.gdx.plugins.provider.PluginsProvider;
import com.gempukku.libgdx.graph.assistant.GdxGraphAssistantPlugin;

public class GraphEditorLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Gdx Assistant");
        config.setWindowedMode(1440, 810);

        new Lwjgl3Application(new GdxAssistant(new LegacyGraphPluginProvider()), config);
    }

    private static class LegacyGraphPluginProvider implements PluginsProvider<AssistantApplication, AssistantPlugin> {
        private Array<AssistantPlugin> plugins = new Array<>();

        public LegacyGraphPluginProvider() {
            plugins.add(new WarningPlugin());
            plugins.add(new GdxGraphAssistantPlugin());
        }

        @Override
        public void loadPlugins() {

        }

        @Override
        public Files getPluginFiles() {
            return Gdx.files;
        }

        @Override
        public Iterable<AssistantPlugin> getPlugins() {
            return plugins;
        }
    }
}
