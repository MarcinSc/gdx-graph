package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class SceneDepthShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SceneDepthShaderNodeConfiguration() {
        super("SceneDepth", "Scene depth", "Provided");
        addNodeInput(
                new DefaultGraphNodeInput("screenPosition", "Screen position", ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("depth", "Depth", ShaderFieldType.Float));
    }
}
