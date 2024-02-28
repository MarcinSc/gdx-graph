package com.gempukku.libgdx.graph.shader.lighting3d.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;

public class EndShadowShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public EndShadowShaderNodeConfiguration() {
        super("ShadowShaderEnd", "Shader output", null);
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, GraphNodeInputSide.Left, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("alpha", "Alpha", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("discardValue", "Discard", false, GraphNodeInputSide.Left, ShaderFieldType.Float));
    }
}
