package com.gempukku.libgdx.graph.plugin.boneanimation.design;

import com.gempukku.libgdx.graph.plugin.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneTransformPropertyBoxProducer;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneWeightPropertyBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class BoneAnimationPlugin {
    public void initialize() {
        BoneAnimationPluginRuntimeInitializer.register();

        UIModelShaderConfiguration.registerPropertyType(
                new BoneWeightPropertyBoxProducer());
        UIModelShaderConfiguration.registerPropertyType(
                new BoneTransformPropertyBoxProducer());
        UIModelShaderConfiguration.register(
                new GraphBoxProducerImpl(new SkinningShaderNodeConfiguration()));
    }
}
