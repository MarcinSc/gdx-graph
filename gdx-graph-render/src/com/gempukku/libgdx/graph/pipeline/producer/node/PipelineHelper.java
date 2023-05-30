package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.gempukku.libgdx.graph.pipeline.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBufferCache;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;

public interface PipelineHelper {
    TextureFrameBufferCache getTextureBufferCache();

    BufferCopyHelper getBufferCopyHelper();

    FullScreenRender getFullScreenRender();

    WhitePixel getWhitePixel();
}
