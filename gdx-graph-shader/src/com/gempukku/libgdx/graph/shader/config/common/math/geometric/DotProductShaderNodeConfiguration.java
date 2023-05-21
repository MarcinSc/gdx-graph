package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class DotProductShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DotProductShaderNodeConfiguration() {
        super("DotProduct", "Dot product", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(ShaderFieldType.Float, "a", "b"),
                        ShaderFieldType.Float));
    }
}
