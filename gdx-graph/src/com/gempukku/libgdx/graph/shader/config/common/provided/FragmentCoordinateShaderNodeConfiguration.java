package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FragmentCoordinateShaderNodeConfiguration extends NodeConfigurationImpl {
    public FragmentCoordinateShaderNodeConfiguration() {
        super("FragmentCoordinate", "Fragment coordinate", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", ShaderFieldType.Vector4));
    }
}
