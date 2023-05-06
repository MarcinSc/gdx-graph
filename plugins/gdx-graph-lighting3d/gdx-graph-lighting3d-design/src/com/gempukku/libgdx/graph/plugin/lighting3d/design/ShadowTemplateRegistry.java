package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class ShadowTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
