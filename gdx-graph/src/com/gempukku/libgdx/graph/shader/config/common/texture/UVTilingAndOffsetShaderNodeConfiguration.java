package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class UVTilingAndOffsetShaderNodeConfiguration extends NodeConfigurationImpl {
    public UVTilingAndOffsetShaderNodeConfiguration() {
        super("UVTilingOffset", "UV Tiling & Offset", "Texture");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("tiling", "Tiling", false, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl("offset", "Offset", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "UV", ShaderFieldType.Vector2));
    }
}
