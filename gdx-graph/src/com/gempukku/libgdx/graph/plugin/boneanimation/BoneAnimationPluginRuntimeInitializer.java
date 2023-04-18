package com.gempukku.libgdx.graph.plugin.boneanimation;

import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneTransformPropertyProducer;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightPropertyProducer;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;

public class BoneAnimationPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        ModelShaderConfiguration.addPropertyProducer(
                new BoneWeightPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(
                new BoneTransformPropertyProducer());

        ModelShaderConfiguration.addNodeBuilder(
                new SkinningShaderNodeBuilder());
    }

    @Override
    public void dispose() {

    }
}
