package com.gempukku.libgdx.graph.shader.boneanimation;

import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneTransformPropertyProducer;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneWeightPropertyProducer;

public class BoneAnimationPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
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
