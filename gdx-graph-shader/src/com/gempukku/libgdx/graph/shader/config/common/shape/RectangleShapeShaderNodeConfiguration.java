package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class RectangleShapeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public RectangleShapeShaderNodeConfiguration() {
        super("RectangleShape", "Rectangle Shape", "Shape");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("border", "Border", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Float));
    }
}
