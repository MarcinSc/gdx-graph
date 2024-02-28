package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ObjectToWorldShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ObjectToWorldShaderNodeConfiguration() {
        super("ObjectToWorld", "Object to World Space", "Model");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", ShaderFieldType.Vector3));
    }
}
