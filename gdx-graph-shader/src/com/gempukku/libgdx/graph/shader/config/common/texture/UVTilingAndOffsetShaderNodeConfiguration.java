package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class UVTilingAndOffsetShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public UVTilingAndOffsetShaderNodeConfiguration() {
        super("UVTilingOffset", "UV Tiling & Offset", "Texture");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("tiling", "Tiling", false, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("offset", "Offset", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "UV", ShaderFieldType.Vector2));
    }
}
