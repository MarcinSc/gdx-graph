package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;

public class ParticlesTemplateRegistry {
    private static Array<GraphTemplate> templates = new Array<>();

    public static void register(GraphTemplate template) {
        templates.add(template);
    }

    static {
        GraphType graphType = GraphTypeRegistry.findGraphType(ParticleEffectGraphType.TYPE);

        register(
                new FileGraphTemplate(graphType, "Empty billboard", Gdx.files.classpath("template/particles/empty-particles-shader.json")));
    }

    public static Iterable<GraphTemplate> getTemplates() {
        return templates;
    }
}
