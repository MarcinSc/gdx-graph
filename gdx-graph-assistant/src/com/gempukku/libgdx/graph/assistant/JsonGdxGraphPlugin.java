package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfigurer;

public class JsonGdxGraphPlugin {
    private String path;

    public JsonGdxGraphPlugin(String path) {
        this.path = path;
    }

    public void initialize() {
        JsonReader reader = new JsonReader();
        FileHandle jsonFile = new InternalFileHandleResolver().resolve(path);
        JsonValue pluginDef = reader.parse(jsonFile);
        UIPipelineConfigurer.processPipelineConfig(pluginDef);
    }
}
