package com.gempukku.libgdx.graph.shader.config.common.value;


import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector4;

public class ValueColorShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueColorShaderNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Vector4));
    }
}
