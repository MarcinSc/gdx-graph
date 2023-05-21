package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class FragmentCoordinateShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public FragmentCoordinateShaderNodeConfiguration() {
        super("FragmentCoordinate", "Fragment coordinate", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector4));
    }
}
