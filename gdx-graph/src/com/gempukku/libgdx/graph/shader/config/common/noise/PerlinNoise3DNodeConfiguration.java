package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PerlinNoise3DNodeConfiguration extends NodeConfigurationImpl {
    public PerlinNoise3DNodeConfiguration() {
        super("PerlinNoise3D", "Perlin Noise 3D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("progress", "Progress", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("range", "Range", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
