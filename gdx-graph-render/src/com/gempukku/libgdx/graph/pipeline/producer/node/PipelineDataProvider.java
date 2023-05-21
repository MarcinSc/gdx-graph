package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBufferCache;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;

public interface PipelineDataProvider {
    <T> T getPrivatePluginData(Class<T> clazz);

    TextureFrameBufferCache getTextureBufferCache();

    BufferCopyHelper getBufferCopyHelper();

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();

    FullScreenRender getFullScreenRender();

    FileHandleResolver getAssetResolver();

    PropertyContainer getRootPropertyContainer();

    WhitePixel getWhitePixel();
}
