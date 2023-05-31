package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineHelper;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public interface PipelineRendererConfiguration {
    void initialize(PipelinePropertySource pipelinePropertySource);

    PipelineHelper getPipelineHelper();

    PipelinePropertySource getPipelinePropertySource();

    WritablePropertyContainer getPipelinePropertyContainer();

    FileHandleResolver getAssetResolver();

    TimeProvider getTimeProvider();

    <T extends RendererConfiguration> T getConfig(Class<T> clazz);

    void startFrame();

    void endFrame();
}
