package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class VoronoiDistance3DNodeConfiguration extends DefaultMenuNodeConfiguration {
    public VoronoiDistance3DNodeConfiguration() {
        super("VoronoiDistance3D", "Voronoi Distance 3D", "Noise");
        addNodeInput(
                new DefaultGraphNodeInput("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("progress", "Progress", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("range", "Range", false, ShaderFieldType.Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Float));
    }
}
