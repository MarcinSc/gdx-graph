package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class Sampler2DShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public Sampler2DShaderNodeConfiguration() {
        super("Sampler2D", "Sampler 2D", "Texture");
        addNodeInput(
                new DefaultGraphNodeInput("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new DefaultGraphNodeOutput("r", "R", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("g", "G", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("b", "B", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("a", "A", ShaderFieldType.Float));
    }
}
