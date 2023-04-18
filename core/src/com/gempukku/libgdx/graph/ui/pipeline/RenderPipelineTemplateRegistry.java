package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineGraphType;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class RenderPipelineTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    static {
        GraphType graphType = GraphTypeRegistry.findGraphType(RenderPipelineGraphType.TYPE);
        register(
                new FileGraphTemplate(graphType, "Empty pipeline",
                        Gdx.files.classpath("template/model/empty-model-shader.json")));
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
