package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class MergeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public MergeShaderNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("x", "X", ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("y", "Y", ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("z", "Z", ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("w", "W", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("v2", "Vector2", ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("v3", "Vector3", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
    }
}
