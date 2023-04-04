package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.gempukku.gdx.assistant.GdxAssistant;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPlugin;
import com.gempukku.gdx.plugins.PluginsProvider;
import com.gempukku.libgdx.graph.assistant.GdxGraphAssistantPlugin;

import java.io.IOException;

public class GraphEditorLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Gdx Assistant";
        config.width = 1440;
        config.height = 810;

        new LwjglApplication(new GdxAssistant(new LegacyGraphPluginProvider()), config);
    }

    private static class LegacyGraphPluginProvider implements PluginsProvider<AssistantApplication, AssistantPlugin> {
        private Array<AssistantPlugin> plugins = new Array<AssistantPlugin>();

        public LegacyGraphPluginProvider() {
            plugins.add(new WarningPlugin());
            plugins.add(new GdxGraphAssistantPlugin());
        }

        @Override
        public Iterable<AssistantPlugin> getPlugins() {
            return plugins;
        }
    }
}
