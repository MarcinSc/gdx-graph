package com.gempukku.libgdx.graph.shader.config.common.noise;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class VoronoiBorder3DNodeConfiguration extends DefaultMenuNodeConfiguration {
    public VoronoiBorder3DNodeConfiguration() {
        super("VoronoiBorder3D", "Voronoi Border 3D", "Noise");
        addNodeInput(
                new DefaultGraphNodeInput("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("scale", "Scale", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("progress", "Progress", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", ShaderFieldType.Float));
    }
}
