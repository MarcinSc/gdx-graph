package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class ShadowTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    static {
        GraphType graphType = GraphTypeRegistry.findGraphType(ShadowShaderGraphType.TYPE);

        register(
                new FileGraphTemplate(graphType, "Empty shadow shader", Gdx.files.classpath("template/shadow/empty-shadow-shader.json")));
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
