package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;

public interface ShaderGraphType extends GraphType {
    GraphConfiguration[] getConfigurations();
}
