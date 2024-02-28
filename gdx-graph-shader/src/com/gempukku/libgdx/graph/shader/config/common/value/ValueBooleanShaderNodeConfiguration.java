package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ValueBooleanShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueBooleanShaderNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", ShaderFieldType.Boolean));
    }
}
