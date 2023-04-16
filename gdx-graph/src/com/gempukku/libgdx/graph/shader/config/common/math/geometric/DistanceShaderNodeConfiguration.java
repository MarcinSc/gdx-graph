package com.gempukku.libgdx.graph.shader.config.common.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class DistanceShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DistanceShaderNodeConfiguration() {
        super("Distance", "Distance", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("p0", "Point 0", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("p1", "Point 1", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new ValidateSameTypeOutputTypeFunction(ShaderFieldType.Float, "p0", "p1"),
                        ShaderFieldType.Float));
    }
}
