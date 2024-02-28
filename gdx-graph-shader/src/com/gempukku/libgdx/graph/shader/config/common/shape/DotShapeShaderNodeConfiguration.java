package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class DotShapeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DotShapeShaderNodeConfiguration() {
        super("DotShape", "Dot Shape", "Shape");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Float));
    }
}
