package com.gempukku.libgdx.graph.shader.screen;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.shader.RenderableModel;

public class GraphScreenShadersImpl implements GraphScreenShaders, RuntimePipelinePlugin {
    private final ObjectMap<String, WritablePropertyContainer> propertyContainers = new ObjectMap<>();
    private FullScreenRenderableModel renderableModel = new FullScreenRenderableModel();

    public void registerTag(String tag) {
        if (propertyContainers.containsKey(tag))
            throw new IllegalStateException("Duplicate screen shader with tag - " + tag);
        propertyContainers.put(tag, new MapWritablePropertyContainer());
    }

    public WritablePropertyContainer getPropertyContainer(String tag) {
        return propertyContainers.get(tag);
    }

    public RenderableModel getRenderableModel(String tag, FullScreenRender fullScreenRender) {
        renderableModel.setFullScreenRender(fullScreenRender);
        return renderableModel;
    }

    @Override
    public void setProperty(String tag, String name, Object value) {
        WritablePropertyContainer propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        propertyContainer.setValue(name, value);
    }

    @Override
    public void unsetProperty(String tag, String name) {
        WritablePropertyContainer propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        propertyContainer.remove(name);
    }

    @Override
    public Object getProperty(String tag, String name) {
        WritablePropertyContainer propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        return propertyContainer.getValue(name);
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }
}
