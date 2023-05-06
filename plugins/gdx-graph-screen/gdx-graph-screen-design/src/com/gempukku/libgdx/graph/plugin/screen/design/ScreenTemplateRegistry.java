package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class ScreenTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
