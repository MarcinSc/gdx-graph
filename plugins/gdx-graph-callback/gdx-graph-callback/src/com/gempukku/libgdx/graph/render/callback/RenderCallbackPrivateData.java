package com.gempukku.libgdx.graph.render.callback;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;

public class RenderCallbackPrivateData implements RenderCallbackPublicData, RuntimePipelinePlugin {
    private final ObjectMap<String, RenderCallback> renderCallbacks = new ObjectMap<>();

    @Override
    public void update(TimeProvider timeProvider) {

    }

    @Override
    public void setRenderCallback(String id, RenderCallback renderCallback) {
        renderCallbacks.put(id, renderCallback);
    }

    public RenderCallback getRenderCallback(String id) {
        return renderCallbacks.get(id);
    }
}
