package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class GraphShaderBuilder {
    public static GraphShader buildShader(GraphWithProperties graph, FileHandleResolver assetResolver) {
        switch (graph.getType()) {
            case ModelShaderGraphType.TYPE:
                return buildModelShader("", assetResolver, graph, false);
            case ShadowShaderGraphType.TYPE:
                return buildModelDepthShader("", assetResolver, graph, false);
            case ScreenShaderGraphType.TYPE:
                return buildScreenShader("", assetResolver, graph, false);
            case ParticleEffectGraphType.TYPE:
                return buildParticlesShader("", assetResolver, graph, false);
        }
        return null;
    }

    public static GraphShader buildModelShader(String tag, FileHandleResolver assetResolver,
                                               GraphWithProperties graph,
                                               boolean designTime) {
        return new ModelGraphShaderRecipe().buildGraphShader(tag, designTime, graph, assetResolver);
    }

    public static GraphShader buildParticlesShader(String tag, FileHandleResolver assetResolver,
                                                   GraphWithProperties graph,
                                                   boolean designTime) {
        return new ParticlesGraphShaderRecipe().buildGraphShader(tag, designTime, graph, assetResolver);
    }

    public static GraphShader buildScreenShader(String tag, FileHandleResolver assetResolver,
                                                GraphWithProperties graph,
                                                boolean designTime) {
        return new ScreenGraphShaderRecipe().buildGraphShader(tag, designTime, graph, assetResolver);
    }

    public static GraphShader buildModelDepthShader(String tag, FileHandleResolver assetResolver,
                                                    GraphWithProperties graph,
                                                    boolean designTime) {
        return new DepthGraphShaderRecipe().buildGraphShader(tag, designTime, graph, assetResolver);
    }
}
