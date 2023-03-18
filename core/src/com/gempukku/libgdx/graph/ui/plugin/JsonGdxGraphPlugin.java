package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfigurer;

public class JsonGdxGraphPlugin implements GdxGraphPlugin {
    private String id;
    private String path;

    public JsonGdxGraphPlugin(String id, String path) {
        this.id = id;
        this.path = path;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(1, 0, 0);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(GdxGraphApplication gdxGraphApplication) {
        JsonReader reader = new JsonReader();
        FileHandle jsonFile = new InternalFileHandleResolver().resolve(path);
        JsonValue pluginDef = reader.parse(jsonFile);
        UIPipelineConfigurer.processPipelineConfig(pluginDef);
    }

    @Override
    public void deregisterPlugin() {

    }
}
