package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class VoronoiBorder3DNodeConfiguration extends NodeConfigurationImpl {
    public VoronoiBorder3DNodeConfiguration() {
        super("VoronoiBorder3D", "Voronoi Border 3D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("progress", "Progress", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
