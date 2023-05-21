package com.gempukku.libgdx.graph.shader.common.sprite;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class BillboardSpriteShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public BillboardSpriteShaderNodeConfiguration() {
        super("BillboardSprite", "Billboard Sprite", "Sprite");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, GraphNodeInputSide.Left, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, GraphNodeInputSide.Left, ShaderFieldType.Vector2));
        addNodeInput(
                new DefaultGraphNodeInput("anchor", "Anchor", false, GraphNodeInputSide.Left, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("size", "Size", false, GraphNodeInputSide.Left, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("rotation", "Rotation", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Vertex Position", ShaderFieldType.Vector3));
    }
}
