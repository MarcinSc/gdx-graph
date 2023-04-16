package com.gempukku.libgdx.graph.plugin.boneanimation.design;

import com.gempukku.libgdx.graph.plugin.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneTransformPropertyEditorDefinition;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneWeightPropertyEditorDefinition;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;

public class BoneAnimationPlugin {
    public void initialize() {
        BoneAnimationPluginRuntimeInitializer.register();

        UIModelShaderConfiguration.registerPropertyType(
                new BoneWeightPropertyEditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(
                new BoneTransformPropertyEditorDefinition());
        UIModelShaderConfiguration.register(
                new DefaultMenuGraphNodeEditorProducer(new SkinningShaderNodeConfiguration()));
    }
}
