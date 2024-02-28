package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class CrossProductShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public CrossProductShaderNodeConfiguration() {
        super("CrossProduct", "Cross product", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("a", "A", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("b", "B", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Vector3));
    }
}
