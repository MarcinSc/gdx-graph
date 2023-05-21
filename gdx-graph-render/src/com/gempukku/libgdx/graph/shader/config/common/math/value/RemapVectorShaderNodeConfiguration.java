package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class RemapVectorShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public RemapVectorShaderNodeConfiguration() {
        super("RemapVector", "Remap vector", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("v2", "Vector2", ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("v3", "Vector3", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
    }
}
