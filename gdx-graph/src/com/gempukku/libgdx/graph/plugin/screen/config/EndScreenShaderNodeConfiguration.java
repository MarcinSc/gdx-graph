package com.gempukku.libgdx.graph.plugin.screen.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;

public class EndScreenShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public EndScreenShaderNodeConfiguration() {
        super("FullScreenShaderEnd", "Shader output", null);
        addNodeInput(
                new DefaultGraphNodeInput("color", "Color", false, GraphNodeInputSide.Left,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alpha", "Alpha", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alphaClip", "Alpha clip", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
    }
}
