package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class VoronoiBorder2DNodeConfiguration extends NodeConfigurationImpl {
    public VoronoiBorder2DNodeConfiguration() {
        super("VoronoiBorder2D", "Voronoi Border 2D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("progress", "Progress", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
