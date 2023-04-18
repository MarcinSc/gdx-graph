package com.gempukku.libgdx.graph.plugin.boneanimation.design;

import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneTransformPropertyEditorDefinition;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneWeightPropertyEditorDefinition;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;

public class UIBoneAnimationPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register property types
        UIModelShaderConfiguration.registerPropertyType(
                new BoneWeightPropertyEditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(
                new BoneTransformPropertyEditorDefinition());
        // Register node editors
        UIModelShaderConfiguration.register(
                new GdxGraphNodeEditorProducer(new SkinningShaderNodeConfiguration()));

        // Register runtime plugin
        RuntimePluginRegistry.register(BoneAnimationPluginRuntimeInitializer.class);
    }
}
