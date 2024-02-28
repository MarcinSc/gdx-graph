package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ScreenPositionShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ScreenPositionShaderNodeConfiguration() {
        super("ScreenPosition", "Screen position", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector2));
    }
}
