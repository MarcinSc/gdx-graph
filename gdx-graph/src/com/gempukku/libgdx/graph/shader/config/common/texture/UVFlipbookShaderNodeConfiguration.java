package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class UVFlipbookShaderNodeConfiguration extends NodeConfigurationImpl {
    public UVFlipbookShaderNodeConfiguration() {
        super("UVFlipbook", "UV Flipbook", "Texture");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("tileCount", "Tile count", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("index", "Index", true, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("looping", "Looping", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "UV", ShaderFieldType.Vector2));
    }
}
