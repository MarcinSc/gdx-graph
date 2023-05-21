package com.gempukku.libgdx.graph.shader.config.common.math.utility;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class DistanceFromPlaneShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public DistanceFromPlaneShaderNodeConfiguration() {
        super("DistancePlane", "Distance from plane", "Math/Utility");
        addNodeInput(
                new DefaultGraphNodeInput("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("planePoint", "Point on plane", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("planeNormal", "Normal to plane", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Distance", ShaderFieldType.Float));
    }
}
