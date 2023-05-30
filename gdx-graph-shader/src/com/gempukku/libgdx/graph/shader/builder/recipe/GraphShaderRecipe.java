package com.gempukku.libgdx.graph.shader.builder.recipe;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;

public interface GraphShaderRecipe {
    boolean isValid(GraphWithProperties graphWithProperties);

    GraphShader buildGraphShader(
            String tag, boolean designTime, GraphWithProperties graphWithProperties, PipelineRendererConfiguration configuration);
}
