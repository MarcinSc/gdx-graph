package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ModelFragmentCoordinateShaderNodeConfiguration extends NodeConfigurationImpl {
    public ModelFragmentCoordinateShaderNodeConfiguration() {
        super("ModelFragmentCoordinate", "Model fragment coordinate", "Model");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Vector2));
    }
}
