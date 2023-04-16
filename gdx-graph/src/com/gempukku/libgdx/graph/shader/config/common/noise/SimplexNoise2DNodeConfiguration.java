package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class SimplexNoise2DNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SimplexNoise2DNodeConfiguration() {
        super("SimplexNoise2D", "Simplex Noise 2D", "Noise");
        addNodeInput(
                new DefaultGraphNodeInput("uv", "UV", true, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("progress", "Progress", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("range", "Range", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Float));
    }
}
