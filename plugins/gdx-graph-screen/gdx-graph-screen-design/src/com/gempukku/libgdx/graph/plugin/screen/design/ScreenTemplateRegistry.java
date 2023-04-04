package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.FileGraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.LoadFileGraphShaderTemplate;

public class ScreenTemplateRegistry {
    private static Array<GraphShaderTemplate> templates = new Array<>();

    public static void register(GraphShaderTemplate template) {
        templates.add(template);
    }

    static {
        register(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/screen/empty-screen-shader.json")));
        register(null);
        register(
                new LoadFileGraphShaderTemplate("From file..."));
    }

    public static Iterable<GraphShaderTemplate> getTemplates() {
        return templates;
    }
}
