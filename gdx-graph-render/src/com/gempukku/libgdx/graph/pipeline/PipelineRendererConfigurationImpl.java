package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineHelper;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public class PipelineRendererConfigurationImpl implements Disposable {
    private final TimeProvider timeProvider;
    private final FileHandleResolver assetResolver;
    private final WritablePropertyContainer pipelinePropertyContainer;

    private final ObjectMap<Class<?>, Object> configs = new ObjectMap<>();

    private PipelinePropertySource pipelinePropertySource;

    private final PipelineHelper pipelineHelper;
    private boolean ownsPipelineHelper;

    public PipelineRendererConfigurationImpl(TimeProvider timeProvider) {
        this(timeProvider, null, null, null);
    }

    public PipelineRendererConfigurationImpl(
            TimeProvider timeProvider, FileHandleResolver assetResolver, WritablePropertyContainer pipelinePropertyContainer,
            PipelineHelper pipelineHelper) {
        this.timeProvider = timeProvider;
        this.assetResolver = assetResolver != null ? assetResolver : new InternalFileHandleResolver();
        this.pipelinePropertyContainer = pipelinePropertyContainer != null ? pipelinePropertyContainer : new MapWritablePropertyContainer();
        if (pipelineHelper != null) {
            this.pipelineHelper = pipelineHelper;
            ownsPipelineHelper = false;
        } else {
            this.pipelineHelper = new SimplePipelineHelper(this.assetResolver);
            ownsPipelineHelper = true;
        }
    }

    public PipelineHelper getPipelineHelper() {
        return pipelineHelper;
    }

    public PipelinePropertySource getPipelinePropertySource() {
        return pipelinePropertySource;
    }

    public void setPipelinePropertySource(PipelinePropertySource pipelinePropertySource) {
        this.pipelinePropertySource = pipelinePropertySource;
    }

    public WritablePropertyContainer getPipelinePropertyContainer() {
        return pipelinePropertyContainer;
    }

    public FileHandleResolver getAssetResolver() {
        return assetResolver;
    }

    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public <T extends RendererConfiguration> void setConfig(Class<T> clazz, T config) {
        configs.put(clazz, config);
    }

    public <T extends RendererConfiguration> T getConfig(Class<T> clazz) {
        return (T) configs.get(clazz);
    }

    public <T extends RendererConfiguration> T removeConfig(Class<T> clazz) {
        return (T) configs.remove(clazz);
    }

    public void startFrame() {
        if (ownsPipelineHelper) {
            ((SimplePipelineHelper) pipelineHelper).startFrame();
        }
    }

    public void endFrame() {
        if (ownsPipelineHelper) {
            ((SimplePipelineHelper) pipelineHelper).endFrame();
        }
    }

    @Override
    public void dispose() {
        if (ownsPipelineHelper) {
            ((SimplePipelineHelper) pipelineHelper).dispose();
        }
        for (Object config : configs.values()) {
            if (config instanceof Disposable) {
                ((Disposable) config).dispose();
            }
        }
        configs.clear();
    }
}
