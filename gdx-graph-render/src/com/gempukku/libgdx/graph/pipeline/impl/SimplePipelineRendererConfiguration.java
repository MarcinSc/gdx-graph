package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.SimplePipelineHelper;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineHelper;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public class SimplePipelineRendererConfiguration implements PipelineRendererConfiguration, Disposable {
    private final TimeProvider timeProvider;
    private final FileHandleResolver assetResolver;
    private final WritablePropertyContainer pipelinePropertyContainer;

    private final ObjectMap<Class<?>, Object> configs = new ObjectMap<>();

    private PipelinePropertySource pipelinePropertySource;

    private final SimplePipelineHelper pipelineHelper;

    public SimplePipelineRendererConfiguration(TimeProvider timeProvider) {
        this(timeProvider, null, null);
    }

    public SimplePipelineRendererConfiguration(
            TimeProvider timeProvider, FileHandleResolver assetResolver, WritablePropertyContainer pipelinePropertyContainer) {
        this.timeProvider = timeProvider;
        this.assetResolver = assetResolver != null ? assetResolver : new InternalFileHandleResolver();
        this.pipelinePropertyContainer = pipelinePropertyContainer != null ? pipelinePropertyContainer : new MapWritablePropertyContainer();
        this.pipelineHelper = new SimplePipelineHelper(this.assetResolver);
    }

    @Override
    public void initialize(PipelinePropertySource pipelinePropertySource) {
        this.pipelinePropertySource = pipelinePropertySource;
    }

    @Override
    public PipelineHelper getPipelineHelper() {
        return pipelineHelper;
    }

    @Override
    public PipelinePropertySource getPipelinePropertySource() {
        return pipelinePropertySource;
    }

    @Override
    public WritablePropertyContainer getPipelinePropertyContainer() {
        return pipelinePropertyContainer;
    }

    @Override
    public FileHandleResolver getAssetResolver() {
        return assetResolver;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public <T extends RendererConfiguration> void setConfig(Class<T> clazz, T config) {
        configs.put(clazz, config);
    }

    @Override
    public <T extends RendererConfiguration> T getConfig(Class<T> clazz) {
        return (T) configs.get(clazz);
    }

    public <T extends RendererConfiguration> T removeConfig(Class<T> clazz) {
        return (T) configs.remove(clazz);
    }

    @Override
    public void startFrame() {
        pipelineHelper.startFrame();
    }

    @Override
    public void endFrame() {
        pipelineHelper.endFrame();
    }

    @Override
    public void dispose() {
        pipelineHelper.dispose();
        for (Object config : configs.values()) {
            if (config instanceof Disposable) {
                ((Disposable) config).dispose();
            }
        }
        configs.clear();
    }
}
