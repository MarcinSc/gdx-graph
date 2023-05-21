package com.gempukku.libgdx.graph.plugin.boneanimation.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneTransformPropertyEditorDefinition;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneWeightPropertyEditorDefinition;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;

public class UIBoneAnimationPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register property types
        UIModelShaderConfiguration.registerPropertyType(
                new BoneWeightPropertyEditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(
                new BoneTransformPropertyEditorDefinition());

        UIModelShaderConfiguration.registerPropertyFunction(BoneWeightFieldType.type, "Bone Weights");

        // Register node editors
        UIModelShaderConfiguration.register(
                new GdxGraphNodeEditorProducer(new SkinningShaderNodeConfiguration()));

        // Register runtime plugin
        RuntimePluginRegistry.register(BoneAnimationPluginRuntimeInitializer.class);
    }
}
