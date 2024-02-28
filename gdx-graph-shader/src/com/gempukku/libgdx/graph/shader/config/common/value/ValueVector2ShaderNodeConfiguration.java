package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector2;

public class ValueVector2ShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueVector2ShaderNodeConfiguration() {
        super("ValueVector2", "Vector2", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Vector2));
    }
}
