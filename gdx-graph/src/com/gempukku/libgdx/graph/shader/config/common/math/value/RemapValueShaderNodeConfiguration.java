package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class RemapValueShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public RemapValueShaderNodeConfiguration() {
        super("RemapValue", "Remap value", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Float));
    }
}
