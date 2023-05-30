package com.gempukku.libgdx.graph.render.callback;

import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;

public interface CallbackRendererConfiguration extends RendererConfiguration {
    RenderCallback getRenderCallback(String id);
}
