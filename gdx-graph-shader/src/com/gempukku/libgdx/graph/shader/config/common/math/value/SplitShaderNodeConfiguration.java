package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class SplitShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SplitShaderNodeConfiguration() {
        super("Split", "Split", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("y", "Y", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("z", "Z", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("w", "W", ShaderFieldType.Float));
    }
}
