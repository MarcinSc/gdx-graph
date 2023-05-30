package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultBufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultTextureFrameBufferCache;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineHelper;
import com.gempukku.libgdx.graph.pipeline.util.FullScreenRenderImpl;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;

public class SimplePipelineHelper implements PipelineHelper, Disposable {
    private final FullScreenRenderImpl fullScreenRender;
    private final DefaultTextureFrameBufferCache textureFrameBufferCache;
    private final DefaultBufferCopyHelper bufferCopyHelper;
    private final WhitePixel whitePixel;

    public SimplePipelineHelper() {
        this(new InternalFileHandleResolver());
    }

    public SimplePipelineHelper(FileHandleResolver assetResolver) {
        textureFrameBufferCache = new DefaultTextureFrameBufferCache();
        bufferCopyHelper = new DefaultBufferCopyHelper(assetResolver);
        fullScreenRender = new FullScreenRenderImpl();
        whitePixel = new WhitePixel();
    }

    @Override
    public TextureFrameBufferCache getTextureBufferCache() {
        return textureFrameBufferCache;
    }

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    @Override
    public FullScreenRender getFullScreenRender() {
        return fullScreenRender;
    }

    @Override
    public WhitePixel getWhitePixel() {
        return whitePixel;
    }

    public void startFrame() {
        textureFrameBufferCache.startFrame();
    }

    public void endFrame() {
        textureFrameBufferCache.endFrame();
    }

    @Override
    public void dispose() {
        textureFrameBufferCache.dispose();
        bufferCopyHelper.dispose();
        fullScreenRender.dispose();
        whitePixel.dispose();
    }
}
