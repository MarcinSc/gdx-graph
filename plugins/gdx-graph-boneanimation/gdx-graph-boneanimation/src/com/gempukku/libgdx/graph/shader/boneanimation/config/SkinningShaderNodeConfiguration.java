package com.gempukku.libgdx.graph.shader.boneanimation.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneTransformFieldType;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class SkinningShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SkinningShaderNodeConfiguration() {
        super("Skinning", "Skinning object", "Model");
        addNodeInput(
                new DefaultGraphNodeInput("position", "Position", false, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("normal", "Normal", false, ShaderFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("boneWeights", "Bone weights", true, BoneWeightFieldType.type));
        addNodeInput(
                new DefaultGraphNodeInput("boneTransformations", "Bone transforms", true, BoneTransformFieldType.type));
        addNodeOutput(
                new DefaultGraphNodeOutput("skinnedPosition", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("skinnedNormal", "Normal", ShaderFieldType.Vector3));
    }
}
