package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class VoronoiDistance2DNodeConfiguration extends NodeConfigurationImpl {
    public VoronoiDistance2DNodeConfiguration() {
        super("VoronoiDistance", "Voronoi Distance 2D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl("uv", "UV", true, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("progress", "Progress", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("range", "Range", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
