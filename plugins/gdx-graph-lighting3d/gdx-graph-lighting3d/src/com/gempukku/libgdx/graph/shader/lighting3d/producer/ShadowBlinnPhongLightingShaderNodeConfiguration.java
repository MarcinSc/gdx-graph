package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ShadowBlinnPhongLightingShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ShadowBlinnPhongLightingShaderNodeConfiguration() {
        super("ShadowBlinnPhongLighting", "Shadowed Blinn-Phong lighting", "Lighting");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("albedo", "Albedo", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("emission", "Emission", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("specular", "Specular", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("ambientOcclusion", "A.Occlusion", false, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("shininess", "Shininess", false, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Color", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("diffuse", "Diffuse", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("specularOut", "Specular", ShaderFieldType.Vector3));
    }
}
