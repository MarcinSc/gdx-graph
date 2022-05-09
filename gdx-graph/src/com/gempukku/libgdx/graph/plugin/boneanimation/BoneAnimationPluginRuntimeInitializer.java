package com.gempukku.libgdx.graph.plugin.boneanimation;

import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneTransformPropertyProducer;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightPropertyProducer;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;

public class BoneAnimationPluginRuntimeInitializer implements PluginRuntimeInitializer {
    public static void register() {
        PluginRegistryImpl.register(BoneAnimationPluginRuntimeInitializer.class);
    }

    public BoneAnimationPluginRuntimeInitializer() {
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        ModelShaderConfiguration.registerPropertyProducer(
                new BoneWeightPropertyProducer());
        ModelShaderConfiguration.registerPropertyProducer(
                new BoneTransformPropertyProducer());

        ModelShaderConfiguration.addGraphShaderNodeBuilder(
                new SkinningShaderNodeBuilder());
    }

    @Override
    public void dispose() {

    }
}
