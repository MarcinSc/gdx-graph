package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ModelFragmentCoordinateShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ModelFragmentCoordinateShaderNodeConfiguration() {
        super("ModelFragmentCoordinate", "Model fragment coordinate", "Model");
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector2));
    }
}
