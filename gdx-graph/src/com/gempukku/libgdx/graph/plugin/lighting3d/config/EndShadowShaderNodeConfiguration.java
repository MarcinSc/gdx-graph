package com.gempukku.libgdx.graph.plugin.lighting3d.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;

public class EndShadowShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public EndShadowShaderNodeConfiguration() {
        super("ShadowShaderEnd", "Shader output", null);
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, GraphNodeInputSide.Left, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("alpha", "Alpha", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alphaClip", "Alpha clip", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
    }
}
