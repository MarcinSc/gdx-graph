package com.gempukku.libgdx.graph.artemis.renderer;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.impl.SharedPipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.ShaderInformation;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;

// This should be on the CameraSystem only, but there is a bug in Artemis
@Wire(failOnNull = false)
public class PipelineRendererSystem extends EntitySystem {
    private final FileHandleResolver assetResolver;
    private final SimplePipelineHelper pipelineHelper;
    private final MapWritablePropertyContainer rootPropertyContainer;
    private final SimpleShaderRendererConfiguration rendererConfiguration;

    private TimeKeepingSystem timeKeepingSystem;
    @Wire(failOnNull = false)
    private CameraSystem cameraSystem;

    private Array<Entity> addedEntities = new Array<>();

    private String renderingPipeline;
    private final ObjectMap<Class<RendererConfiguration>, RendererConfiguration> configurations = new ObjectMap<>();
    private final ObjectMap<String, PipelineRenderer> pipelineRendererMap = new ObjectMap<>();

    public PipelineRendererSystem() {
        this(new InternalFileHandleResolver());
    }

    public PipelineRendererSystem(FileHandleResolver assetResolver) {
        super(Aspect.all(PipelineRendererComponent.class));
        this.assetResolver = assetResolver;
        this.pipelineHelper = new SimplePipelineHelper();
        this.rootPropertyContainer = new MapWritablePropertyContainer();
        this.rendererConfiguration = new SimpleShaderRendererConfiguration(rootPropertyContainer);
    }

    @Override
    public void inserted(Entity e) {
        addedEntities.add(e);
    }

    @Override
    public void removed(Entity e) {
        addedEntities.removeValue(e, true);
        PipelineRendererComponent pipelineRendererComponent = e.getComponent(PipelineRendererComponent.class);
        String pipelineName = pipelineRendererComponent.getPipelineName();
        removePipelineRenderer(pipelineName);
    }

    public void setRenderingPipeline(String pipelineName) {
        this.renderingPipeline = pipelineName;
    }

    public void setPipelineProperty(String name, Object value) {
        rootPropertyContainer.setValue(name, value);
    }

    public void setGlobalUniform(String tag, String name, Object value) {
        rendererConfiguration.getGlobalUniforms(tag).setValue(name, value);
    }

    public float getPipelineTime(String pipelineName) {
        return pipelineRendererMap.get(pipelineName).getConfiguration().getTimeProvider().getTime();
    }

    @Override
    protected void processSystem() {
        if (addedEntities.size>0) {
            for (Entity addedEntity : addedEntities) {
                PipelineRendererComponent pipelineRendererComponent = addedEntity.getComponent(PipelineRendererComponent.class);
                String pipelineName = pipelineRendererComponent.getPipelineName();
                String pipelinePath = pipelineRendererComponent.getPipelinePath();

                PipelineRenderer pipelineRenderer = addPipelineRenderer(addedEntity, pipelineName, pipelinePath, pipelineRendererComponent.isRenderingPipeline());

                WritablePropertyContainer propertyContainer = pipelineRenderer.getConfiguration().getPipelinePropertyContainer();
                for (CameraDefinition cameraDefinition : pipelineRendererComponent.getCameraDefinitions()) {
                    propertyContainer.setValue(cameraDefinition.getCameraProperty(), cameraSystem.getCamera(cameraDefinition.getCameraName()));
                }
            }

            addedEntities.clear();
        }

        if (renderingPipeline != null && pipelineRendererMap.containsKey(renderingPipeline)) {
            pipelineHelper.startFrame();
            PipelineRenderer pipelineRenderer = pipelineRendererMap.get(renderingPipeline);
            pipelineRenderer.render(RenderOutputs.drawToScreen);
            pipelineHelper.endFrame();
        }
    }

    public PipelineRenderer addPipelineRenderer(Entity timeEntity, String pipelineName, String pipelinePath, boolean renderingPipeline) {
        DefaultTimeKeeper timeProvider = timeKeepingSystem.getTimeProvider(timeEntity);

        SharedPipelineRendererConfiguration configuration = new SharedPipelineRendererConfiguration(timeProvider, assetResolver, rootPropertyContainer, pipelineHelper);
        configuration.setConfig(ShaderRendererConfiguration.class, rendererConfiguration);
        for (ObjectMap.Entry<Class<RendererConfiguration>, RendererConfiguration> pluginConfiguration : configurations) {
            configuration.setConfig(pluginConfiguration.key, pluginConfiguration.value);
        }

        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(assetResolver.resolve(pipelinePath), configuration);
        pipelineRendererMap.put(pipelineName, pipelineRenderer);

        if (renderingPipeline) {
            setRenderingPipeline(pipelineName);
        }

        return pipelineRenderer;
    }

    public void removePipelineRenderer(String pipelineName) {
        PipelineRenderer removed = pipelineRendererMap.remove(pipelineName);
        removed.dispose();
        if (pipelineName.equals(renderingPipeline))
            renderingPipeline = null;
    }

    public <T extends RendererConfiguration> void addRendererConfiguration(Class<T> clazz, T configuration) {
        configurations.put((Class<RendererConfiguration>) clazz, configuration);
    }

    private SimpleShaderRendererConfiguration getShaderConfiguration() {
        return rendererConfiguration;
    }

    public ShaderInformation getShaderInformation() {
        return getShaderConfiguration();
    }

    public ModelContainer<RenderableModel> getModelContainer() {
        return getShaderConfiguration();
    }

    @Override
    public void dispose() {
        for (PipelineRenderer value : pipelineRendererMap.values()) {
            value.dispose();
        }
        pipelineHelper.dispose();
    }
}
