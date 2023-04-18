package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class ScreenTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    static {
        GraphType graphType = GraphTypeRegistry.findGraphType(ScreenShaderGraphType.TYPE);

        register(
                new FileGraphTemplate(graphType, "Empty screen shader", Gdx.files.classpath("template/screen/empty-screen-shader.json")));
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
