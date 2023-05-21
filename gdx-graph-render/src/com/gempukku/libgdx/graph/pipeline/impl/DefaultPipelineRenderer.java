package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;
import com.gempukku.libgdx.graph.pipeline.shader.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;

public class DefaultPipelineRenderer implements PipelineRenderer {
    private final TimeProvider timeProvider;
    private PreparedRenderingPipeline preparedRenderingPipeline;
    private final ObjectMap<String, ? extends PipelineProperty> pipelinePropertyMap;
    private final PipelineRenderingContextImpl pipelineRenderingContext;
    private final RuntimePluginRegistry pluginRegistry;
    private final boolean ownsResources;
    private final PipelineRendererResources resources;
    private final WhitePixel whitePixel;

    public DefaultPipelineRenderer(RuntimePluginRegistry pluginRegistry, TimeProvider timeProvider,
                                   PreparedRenderingPipeline preparedRenderingPipeline, ObjectMap<String, DefaultPipelineProperty> pipelinePropertyMap) {
        this(pluginRegistry, timeProvider, preparedRenderingPipeline, pipelinePropertyMap, null);
    }

    public DefaultPipelineRenderer(RuntimePluginRegistry pluginRegistry, TimeProvider timeProvider,
                                   PreparedRenderingPipeline preparedRenderingPipeline,
                                   ObjectMap<String, ? extends PipelineProperty> pipelinePropertyMap,
                                   PipelineRendererResources resources) {
        whitePixel = new WhitePixel();

        this.pluginRegistry = pluginRegistry;
        this.timeProvider = timeProvider;
        this.preparedRenderingPipeline = preparedRenderingPipeline;
        this.pipelinePropertyMap = pipelinePropertyMap;
        if (resources != null) {
            ownsResources = false;
            this.resources = resources;
        } else {
            ownsResources = true;
            this.resources = new DefaultPipelineRendererResources(new InternalFileHandleResolver());
        }
        pipelineRenderingContext = new PipelineRenderingContextImpl();

        preparedRenderingPipeline.initialize(pipelineRenderingContext);
    }

    @Override
    public void setPipelineProperty(String property, Object value) {
        PipelineProperty propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue != null) {
            PipelineFieldType fieldType = PipelineFieldTypeRegistry.findPipelineFieldType(propertyValue.getType());
            if (!fieldType.accepts(value))
                throw new IllegalArgumentException("Property value not accepted, property: " + property);
        }
        resources.getRootPropertyContainer().setValue(property, value);
    }

    @Override
    public boolean hasPipelineProperty(String property) {
        return pipelinePropertyMap.containsKey(property);
    }

    @Override
    public void unsetPipelineProperty(String property) {
        resources.getRootPropertyContainer().remove(property);
    }

    @Override
    public PipelineProperty getPipelineProperty(String property) {
        return pipelinePropertyMap.get(property);
    }

    @Override
    public Iterable<? extends PipelineProperty> getProperties() {
        return pipelinePropertyMap.values();
    }

    @Override
    public <T> T getPluginData(Class<T> clazz) {
        return pluginRegistry.getPublicData(clazz);
    }

    @Override
    public void render(final RenderOutput renderOutput) {
        // Some platforms are chocking (throwing Exception) when asked to create
        // a Render Buffer with a dimension of 0
        if (renderOutput.getRenderWidth() != 0 && renderOutput.getRenderHeight() != 0) {
            pipelineRenderingContext.setRenderOutput(renderOutput);
            if (ownsResources) {
                ((DefaultPipelineRendererResources) resources).startFrame();
            }

            preparedRenderingPipeline.startFrame();

            pipelineRenderingContext.update();

            pipelineRenderingContext.getRenderContext().begin();

            // Execute nodes
            RenderPipeline renderPipeline = preparedRenderingPipeline.execute(pipelineRenderingContext);
            renderOutput.output(renderPipeline, pipelineRenderingContext, pipelineRenderingContext.getFullScreenRender());
            renderPipeline.destroyDefaultBuffer();
            pipelineRenderingContext.getRenderContext().end();

            preparedRenderingPipeline.endFrame();

            if (ownsResources) {
                ((DefaultPipelineRendererResources) resources).endFrame();
            }
        }
    }

    @Override
    public void dispose() {
        whitePixel.dispose();
        preparedRenderingPipeline.dispose();
        if (ownsResources)
            ((DefaultPipelineRendererResources) resources).dispose();
        pluginRegistry.dispose();
    }

    private class PipelineRenderingContextImpl implements PipelineRenderingContext, PipelineDataProvider {
        private final OpenGLContext renderContext = new StateOpenGLContext();
        private RenderOutput renderOutput;

        public void setRenderOutput(RenderOutput renderOutput) {
            this.renderOutput = renderOutput;
        }

        public void update() {
            pluginRegistry.update(timeProvider);
        }

        @Override
        public int getRenderWidth() {
            return renderOutput.getRenderWidth();
        }

        @Override
        public int getRenderHeight() {
            return renderOutput.getRenderHeight();
        }

        @Override
        public <T> T getPrivatePluginData(Class<T> clazz) {
            return pluginRegistry.getPrivateData(clazz);
        }

        @Override
        public TextureFrameBufferCache getTextureBufferCache() {
            return resources.getTextureFrameBufferCache();
        }

        @Override
        public BufferCopyHelper getBufferCopyHelper() {
            return resources.getBufferCopyHelper();
        }

        @Override
        public PipelinePropertySource getPipelinePropertySource() {
            return DefaultPipelineRenderer.this;
        }

        @Override
        public TimeProvider getTimeProvider() {
            return timeProvider;
        }

        @Override
        public OpenGLContext getRenderContext() {
            return renderContext;
        }

        @Override
        public FullScreenRender getFullScreenRender() {
            return resources.getFullScreenRender();
        }

        @Override
        public FileHandleResolver getAssetResolver() {
            return resources.getAssetResolver();
        }

        @Override
        public PropertyContainer getRootPropertyContainer() {
            return resources.getRootPropertyContainer();
        }

        @Override
        public WhitePixel getWhitePixel() {
            return whitePixel;
        }
    }
}
