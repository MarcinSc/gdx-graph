package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.render.callback.CallbackRendererConfiguration;
import com.gempukku.libgdx.graph.render.callback.RenderCallback;

public class SimpleCallbackRendererConfiguration implements CallbackRendererConfiguration {
    private final ObjectMap<String, RenderCallback> callbackMap = new ObjectMap<>();

    public void setRenderCallback(String id, RenderCallback callback) {
        callbackMap.put(id, callback);
    }

    @Override
    public RenderCallback getRenderCallback(String id) {
        return callbackMap.get(id);
    }
}
