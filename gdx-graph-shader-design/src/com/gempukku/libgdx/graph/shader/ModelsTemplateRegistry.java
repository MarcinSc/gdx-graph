package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;


public class ModelsTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
