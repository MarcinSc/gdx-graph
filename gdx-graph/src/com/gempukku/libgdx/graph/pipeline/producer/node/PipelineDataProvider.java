package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.impl.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.impl.TextureFrameBufferCache;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.graph.util.WhitePixel;

public interface PipelineDataProvider {
    <T> T getPrivatePluginData(Class<T> clazz);

    TextureFrameBufferCache getTextureBufferCache();

    BufferCopyHelper getBufferCopyHelper();

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();

    FullScreenRender getFullScreenRender();

    FileHandleResolver getAssetResolver();

    WhitePixel getWhitePixel();
}
