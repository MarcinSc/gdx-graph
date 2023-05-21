package com.gempukku.libgdx.graph.plugin.postprocess.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.postprocess.design.producer.DepthOfFieldEditorProducer;
import com.gempukku.libgdx.graph.render.postprocess.producer.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.render.postprocess.producer.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.render.postprocess.producer.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIPostprocessPlugin implements UIGdxGraphPlugin {
    public void initialize(FileHandleResolver assetResolver) {
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new BloomPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new GaussianBlurPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new DepthOfFieldEditorProducer());
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new GammaCorrectionPipelineNodeConfiguration()));
    }
}
