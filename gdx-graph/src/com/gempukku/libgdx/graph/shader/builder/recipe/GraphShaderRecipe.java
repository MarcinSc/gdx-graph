package com.gempukku.libgdx.graph.shader.builder.recipe;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;

public interface GraphShaderRecipe {
    GraphShader buildGraphShader(
            String tag, boolean designTime, GraphWithProperties graphWithProperties, FileHandleResolver assetResolver);
}
