package com.gempukku.libgdx.graph.shader.common.sprite;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ScreenSpriteShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ScreenSpriteShaderNodeConfiguration() {
        super("ScreenSprite", "Screen Sprite", "Sprite");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, false, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("anchor", "Anchor", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("rotation", "Rotation", false, false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Vertex Position", ShaderFieldType.Vector3));
    }
}
