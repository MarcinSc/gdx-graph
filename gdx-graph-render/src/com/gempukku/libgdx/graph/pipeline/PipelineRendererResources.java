package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;

public interface PipelineRendererResources {
    BufferCopyHelper getBufferCopyHelper();

    TextureFrameBufferCache getTextureFrameBufferCache();

    FullScreenRender getFullScreenRender();

    FileHandleResolver getAssetResolver();

    WritablePropertyContainer getRootPropertyContainer();
}
