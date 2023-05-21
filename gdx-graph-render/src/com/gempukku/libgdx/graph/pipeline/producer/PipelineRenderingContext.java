package com.gempukku.libgdx.graph.pipeline.producer;

import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    OpenGLContext getRenderContext();
}
