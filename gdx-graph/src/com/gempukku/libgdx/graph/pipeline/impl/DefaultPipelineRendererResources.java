package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererResources;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;

public class DefaultPipelineRendererResources implements PipelineRendererResources, Disposable {
    private final WritablePropertyContainer propertyContainer;
    private final DefaultBufferCopyHelper bufferCopyHelper;
    private final DefaultTextureFrameBufferCache textureFrameBufferCache;
    private final FullScreenRenderImpl fullScreenRender;
    private final FileHandleResolver assetResolver;

    public DefaultPipelineRendererResources(FileHandleResolver assetResolver) {
        this.assetResolver = assetResolver;
        propertyContainer = new MapWritablePropertyContainer();
        bufferCopyHelper = new DefaultBufferCopyHelper(assetResolver);
        textureFrameBufferCache = new DefaultTextureFrameBufferCache();
        fullScreenRender = new FullScreenRenderImpl();
    }

    public void startFrame() {
        textureFrameBufferCache.startFrame();
    }

    public void endFrame() {
        textureFrameBufferCache.endFrame();
    }

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    @Override
    public DefaultTextureFrameBufferCache getTextureFrameBufferCache() {
        return textureFrameBufferCache;
    }

    @Override
    public FullScreenRenderImpl getFullScreenRender() {
        return fullScreenRender;
    }

    @Override
    public FileHandleResolver getAssetResolver() {
        return assetResolver;
    }

    @Override
    public WritablePropertyContainer getRootPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void dispose() {
        fullScreenRender.dispose();
        textureFrameBufferCache.dispose();
        bufferCopyHelper.dispose();
    }
}
