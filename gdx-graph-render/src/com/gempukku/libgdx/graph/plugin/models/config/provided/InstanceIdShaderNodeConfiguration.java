package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class InstanceIdShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public InstanceIdShaderNodeConfiguration() {
        super("InstanceID", "Instance ID", "Model");
        addNodeOutput(
                new DefaultGraphNodeOutput("id", "Id", ShaderFieldType.Float));
    }
}
