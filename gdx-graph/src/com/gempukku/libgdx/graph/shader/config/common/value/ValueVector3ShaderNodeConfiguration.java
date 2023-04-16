package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector3;

public class ValueVector3ShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueVector3ShaderNodeConfiguration() {
        super("ValueVector3", "Vector3", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Vector3));
    }
}
