package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;

public class GraphShaderBuilder {
    public static GraphShader buildShader(GraphWithProperties graph, FileHandleResolver assetResolver) {
        return GraphTypeRegistry.findGraphType(graph.getType(), ShaderGraphType.class).buildGraphShader("", assetResolver, graph, false);
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
