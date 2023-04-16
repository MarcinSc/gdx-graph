package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class PixelSizeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public PixelSizeShaderNodeConfiguration() {
        super("PixelSize", "Pixel size", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("size", "Size", ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("y", "Y", ShaderFieldType.Float));
    }
}
