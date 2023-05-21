package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;

public interface ShaderGraphType extends GraphType {
    GraphShader buildGraphShader(String tag, FileHandleResolver assetResolver, GraphWithProperties graph, boolean designTime);
    GraphConfiguration[] getConfigurations();
}
