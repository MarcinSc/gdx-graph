package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class UVFlipbookShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public UVFlipbookShaderNodeConfiguration() {
        super("UVFlipbook", "UV Flipbook", "Texture");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("tileCount", "Tile count", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("index", "Index", true, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("looping", "Looping", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "UV", ShaderFieldType.Vector2));
    }
}
