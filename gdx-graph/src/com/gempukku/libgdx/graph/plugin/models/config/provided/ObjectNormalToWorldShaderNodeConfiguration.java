package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ObjectNormalToWorldShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ObjectNormalToWorldShaderNodeConfiguration() {
        super("ObjectNormalToWorld", "Object normal to World Space", "Model");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector3));
    }
}
