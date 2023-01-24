package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class TextureSizeShaderNodeConfiguration extends NodeConfigurationImpl {
    public TextureSizeShaderNodeConfiguration() {
        super("TextureSize", "Texture Size", "Texture");
        addNodeInput(
                new GraphNodeInputImpl("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeOutput(
                new GraphNodeOutputImpl("size", "Size", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("width", "Width", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("height", "Height", ShaderFieldType.Float));
    }
}
