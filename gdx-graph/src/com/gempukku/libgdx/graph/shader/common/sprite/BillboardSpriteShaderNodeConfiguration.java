package com.gempukku.libgdx.graph.shader.common.sprite;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class BillboardSpriteShaderNodeConfiguration extends NodeConfigurationImpl {
    public BillboardSpriteShaderNodeConfiguration() {
        super("BillboardSprite", "Billboard Sprite", "Sprite");
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, false, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("anchor", "Anchor", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("size", "Size", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("rotation", "Rotation", false, false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Vertex Position", ShaderFieldType.Vector3));
    }
}
