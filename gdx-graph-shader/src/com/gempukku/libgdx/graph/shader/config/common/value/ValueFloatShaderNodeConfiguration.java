package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ValueFloatShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueFloatShaderNodeConfiguration() {
        super("ValueFloat", "Float", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", ShaderFieldType.Float));
    }
}
