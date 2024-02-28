package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class WorldPositionShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public WorldPositionShaderNodeConfiguration() {
        super("WorldPosition", "Fragment world position", "Model");
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector3));
    }
}
