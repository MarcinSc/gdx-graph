package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class StarShapeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public StarShapeShaderNodeConfiguration() {
        super("StarShape", "Star Shape", "Shape");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("arms", "Arms", true, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("minDepth", "Min depth", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("maxDepth", "Max depth", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("curve", "Curve", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Float));
    }
}
