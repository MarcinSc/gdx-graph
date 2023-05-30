package com.gempukku.libgdx.graph.artemis.renderer;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.ShaderInformation;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;

// This should be on the CameraSystem only, but there is a bug in Artemis
@Wire(failOnNull = false)
public class PipelineRendererSystem extends BaseEntitySystem {
    private final SimpleTimeProvider simpleTimeProvider = new SimpleTimeProvider();

    private MapWritablePropertyContainer rootPropertyContainer;
    private SimpleShaderRendererConfiguration simpleShaderRenderingConfiguration;
    private PipelineRenderer pipelineRenderer;

    private TimeKeepingSystem timeKeepingSystem;
    @Wire(failOnNull = false)
    private CameraSystem cameraSystem;

    private Entity renderingEntity;

    private boolean initializedRenderer;
    private boolean renderingEnabled = true;

    private final ObjectMap<Class<RendererConfiguration>, RendererConfiguration> configurations = new ObjectMap<>();

    private PipelineRendererConfiguration configuration;

    public PipelineRendererSystem() {
        super(Aspect.all(PipelineRendererComponent.class));
    }

    @Override
    protected void inserted(int entityId) {
        renderingEntity = world.getEntity(entityId);
    }

    public void setRenderingEnabled(boolean renderingEnabled) {
        this.renderingEnabled = renderingEnabled;
    }

    public void setPipelineProperty(String name, Object value) {
        if (!initializedRenderer)
            throw new GdxRuntimeException("Renderer not yet initialized");

        rootPropertyContainer.setValue(name, value);
    }

    public void setGlobalUniform(String tag, String name, Object value) {
        if (!initializedRenderer)
            throw new GdxRuntimeException("Renderer not yet initialized");

        simpleShaderRenderingConfiguration.getGlobalUniforms(tag).setValue(name, value);
    }

    public float getCurrentTime() {
        return timeKeepingSystem.getTimeProvider(renderingEntity).getTime();
    }

    @Override
    protected void processSystem() {
        if (!initializedRenderer) {
            PipelineRendererComponent pipelineRendererComponent = renderingEntity.getComponent(PipelineRendererComponent.class);

            InternalFileHandleResolver assetResolver = new InternalFileHandleResolver();

            rootPropertyContainer = new MapWritablePropertyContainer();
            simpleShaderRenderingConfiguration = new SimpleShaderRendererConfiguration(rootPropertyContainer);

            for (CameraDefinition cameraDefinition : pipelineRendererComponent.getCameraDefinitions()) {
                rootPropertyContainer.setValue(cameraDefinition.getCameraProperty(), cameraSystem.getCamera(cameraDefinition.getCameraName()));
            }

            configuration = new PipelineRendererConfiguration(simpleTimeProvider, assetResolver, rootPropertyContainer, null);
            configuration.setConfig(ShaderRendererConfiguration.class, simpleShaderRenderingConfiguration);
            for (ObjectMap.Entry<Class<RendererConfiguration>, RendererConfiguration> pluginConfiguration : configurations) {
                configuration.setConfig(pluginConfiguration.key, pluginConfiguration.value);
            }
            configurations.clear();

            pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.internal(pipelineRendererComponent.getPipelinePath()), configuration);

            initializedRenderer = true;
        }

        if (renderingEnabled) {
            TimeProvider timeProvider = timeKeepingSystem.getTimeProvider(renderingEntity);
            simpleTimeProvider.setDelta(timeProvider.getDelta());
            simpleTimeProvider.setTime(timeProvider.getTime());
            pipelineRenderer.render(RenderOutputs.drawToScreen);
        }
    }

    public <T extends RendererConfiguration> void addRendererConfiguration(Class<T> clazz, T configuration) {
        if (initializedRenderer)
            throw new GdxRuntimeException("Renderer has already been initialized");
        configurations.put((Class<RendererConfiguration>) clazz, configuration);
    }

    public ShaderInformation getShaderInformation() {
        return simpleShaderRenderingConfiguration;
    }

    public ModelContainer<RenderableModel> getModelContainer() {
        return simpleShaderRenderingConfiguration;
    }

    @Override
    public void dispose() {
        if (pipelineRenderer != null)
            pipelineRenderer.dispose();
        if (configuration != null)
            configuration.dispose();
    }
}
