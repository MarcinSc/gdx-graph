package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class SceneColorShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SceneColorShaderNodeConfiguration() {
        super("SceneColor", "Scene color", "Provided");
        addNodeOutput(
                new DefaultGraphNodeOutput("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
