package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PhongLightingShaderNodeConfiguration extends NodeConfigurationImpl {
    public PhongLightingShaderNodeConfiguration() {
        super("PhongLighting", "Phong lighting", "Lighting");
        addNodeInput(
                new GraphNodeInputImpl("position", "Position", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("albedo", "Albedo", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("emission", "Emission", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("specular", "Specular", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("ambientOcclusion", "A.Occlusion", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("shininess", "Shininess", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Color", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("diffuse", "Diffuse", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("specularOut", "Specular", ShaderFieldType.Vector3));
    }
}
