package com.gempukku.libgdx.graph.shader.config.common.math.trigonometry;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class Arctan2ShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public Arctan2ShaderNodeConfiguration() {
        super("Arctan2", "Arctangent2", "Math/Trigonometry");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Float));
    }
}
