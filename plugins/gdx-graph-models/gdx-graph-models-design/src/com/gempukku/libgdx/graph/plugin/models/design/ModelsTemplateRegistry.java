package com.gempukku.libgdx.graph.plugin.models.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;


public class ModelsTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    static {
        GraphType graphType = GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE);
        register(
                new FileGraphTemplate(graphType, "Empty model shader", Gdx.files.classpath("template/model/empty-model-shader.json")));
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
