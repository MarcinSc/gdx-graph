package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;

public interface ShaderGraphType extends GraphType {
    GraphShader buildGraphShader(String tag, PipelineRendererConfiguration configuration, GraphWithProperties graph, boolean designTime);

    GraphConfiguration[] getConfigurations();

    GraphValidationResult validateGraph(Graph graph, String startingNode);
}
