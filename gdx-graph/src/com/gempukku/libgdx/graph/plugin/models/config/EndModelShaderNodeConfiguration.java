package com.gempukku.libgdx.graph.plugin.models.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;

public class EndModelShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public EndModelShaderNodeConfiguration() {
        super("ShaderEnd", "Shader output", null);
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, false, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("color", "Color", false, false, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
