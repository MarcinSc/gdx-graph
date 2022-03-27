package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.design.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.FileGraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.LoadFileGraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.PasteGraphShaderTemplate;

public class ShadowTemplateRegistry {
    private static Array<GraphShaderTemplate> templates = new Array<>();

    public static void register(GraphShaderTemplate template) {
        templates.add(template);
    }

    static {
        register(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/shadow/empty-shadow-shader.json")));
        register(null);
        register(
                new LoadFileGraphShaderTemplate("From file..."));
        register(
                new PasteGraphShaderTemplate(ModelShaderGraphType.instance));
    }

    public static Iterable<GraphShaderTemplate> getTemplates() {
        return templates;
    }
}
