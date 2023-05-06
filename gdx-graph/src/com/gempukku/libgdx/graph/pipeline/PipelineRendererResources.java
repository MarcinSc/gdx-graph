package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.impl.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.impl.TextureFrameBufferCache;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;

public class PipelineRendererResources implements Disposable {
    private final BufferCopyHelper bufferCopyHelper;
    private final TextureFrameBufferCache textureFrameBufferCache;
    private final FullScreenRenderImpl fullScreenRender;
    private final FileHandleResolver assetResolver;

    public PipelineRendererResources(FileHandleResolver assetResolver) {
        this.assetResolver = assetResolver;
        bufferCopyHelper = new BufferCopyHelper(assetResolver);
        textureFrameBufferCache = new TextureFrameBufferCache();
        fullScreenRender = new FullScreenRenderImpl();
    }

    public void startFrame() {
        textureFrameBufferCache.startFrame();
    }

    public void endFrame() {
        textureFrameBufferCache.endFrame();
    }

    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    public TextureFrameBufferCache getTextureFrameBufferCache() {
        return textureFrameBufferCache;
    }

    public FullScreenRenderImpl getFullScreenRender() {
        return fullScreenRender;
    }

    public FileHandleResolver getAssetResolver() {
        return assetResolver;
    }

    @Override
    public void dispose() {
        fullScreenRender.dispose();
        textureFrameBufferCache.dispose();
        bufferCopyHelper.dispose();
    }
}
