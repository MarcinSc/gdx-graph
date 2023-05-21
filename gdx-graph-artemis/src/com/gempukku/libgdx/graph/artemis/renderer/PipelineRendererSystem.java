package com.gempukku.libgdx.graph.artemis.renderer;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;

// This should be on the CameraSystem only, but there is a bug in Artemis
@Wire(failOnNull = false)
public class PipelineRendererSystem extends BaseEntitySystem {
    private final SimpleTimeProvider simpleTimeProvider = new SimpleTimeProvider();
    private PipelineRenderer pipelineRenderer;

    private TimeKeepingSystem timeKeepingSystem;
    @Wire(failOnNull = false)
    private CameraSystem cameraSystem;

    private Entity renderingEntity;

    private boolean initializedRenderer;
    private boolean renderingEnabled = true;

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

        pipelineRenderer.setPipelineProperty(name, value);
    }

    public float getCurrentTime() {
        return timeKeepingSystem.getTimeProvider(renderingEntity).getTime();
    }

    @Override
    protected void processSystem() {
        if (!initializedRenderer) {
            PipelineRendererComponent pipelineRendererComponent = renderingEntity.getComponent(PipelineRendererComponent.class);
            pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.internal(pipelineRendererComponent.getPipelinePath()), simpleTimeProvider);
            for (CameraDefinition cameraDefinition : pipelineRendererComponent.getCameraDefinitions()) {
                pipelineRenderer.setPipelineProperty(cameraDefinition.getCameraProperty(), cameraSystem.getCamera(cameraDefinition.getCameraName()));
            }

            initializedRenderer = true;
        }

        if (renderingEnabled) {
            TimeProvider timeProvider = timeKeepingSystem.getTimeProvider(renderingEntity);
            simpleTimeProvider.setDelta(timeProvider.getDelta());
            simpleTimeProvider.setTime(timeProvider.getTime());
            pipelineRenderer.render(RenderOutputs.drawToScreen);
        }
    }

    public <T> T getPluginData(Class<T> clazz) {
        if (!initializedRenderer)
            throw new GdxRuntimeException("Renderer not yet initialized");
        return pipelineRenderer.getPluginData(clazz);
    }

    @Override
    public void dispose() {
        if (pipelineRenderer != null)
            pipelineRenderer.dispose();
    }
}
