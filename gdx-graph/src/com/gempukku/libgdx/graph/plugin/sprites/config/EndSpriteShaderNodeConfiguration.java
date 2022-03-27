package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EndSpriteShaderNodeConfiguration extends NodeConfigurationImpl {
    public EndSpriteShaderNodeConfiguration() {
        super("SpriteShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl("position", "Vertex position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("color", "Color", false, false,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
