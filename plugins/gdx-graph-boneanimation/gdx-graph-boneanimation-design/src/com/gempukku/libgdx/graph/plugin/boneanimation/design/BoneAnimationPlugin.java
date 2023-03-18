package com.gempukku.libgdx.graph.plugin.boneanimation.design;

import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.boneanimation.BoneAnimationPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneTransformPropertyBoxProducer;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.producer.BoneWeightPropertyBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class BoneAnimationPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-bone-animation";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(1, 0, 0);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(GdxGraphApplication gdxGraphApplication) {
        BoneAnimationPluginRuntimeInitializer.register();

        UIModelShaderConfiguration.registerPropertyType(
                new BoneWeightPropertyBoxProducer());
        UIModelShaderConfiguration.registerPropertyType(
                new BoneTransformPropertyBoxProducer());
        UIModelShaderConfiguration.register(
                new GraphBoxProducerImpl(new SkinningShaderNodeConfiguration()));
    }

    @Override
    public void deregisterPlugin() {

    }
}
