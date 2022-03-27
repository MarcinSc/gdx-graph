package com.gempukku.libgdx.graph.plugin.sprites.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.FileGraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.LoadFileGraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.PasteGraphShaderTemplate;

public class SpritesTemplateRegistry {
    private static Array<GraphShaderTemplate> templates = new Array<>();

    public static void register(GraphShaderTemplate template) {
        templates.add(template);
    }

    static {
        register(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/sprite/empty-sprite-shader.json")));
        register(null);
        register(
                new FileGraphShaderTemplate("Animated Sprite", Gdx.files.classpath("template/sprite/animated-sprite-shader.json")));
        register(
                new FileGraphShaderTemplate("Tiled Sprite", Gdx.files.classpath("template/sprite/tiled-sprite-shader.json")));
        register(null);
        register(
                new LoadFileGraphShaderTemplate("From file..."));
        register(
                new PasteGraphShaderTemplate(SpriteShaderGraphType.instance));
    }

    public static Iterable<GraphShaderTemplate> getTemplates() {
        return templates;
    }
}
