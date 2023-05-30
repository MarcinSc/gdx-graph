package com.gempukku.libgdx.graph.pipeline.impl;

import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;
import com.gempukku.libgdx.graph.pipeline.shader.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;

public class DefaultPipelineRenderer implements PipelineRenderer, PipelineRenderingContext {
    private final OpenGLContext renderContext = new StateOpenGLContext();
    private final PreparedRenderingPipeline preparedRenderingPipeline;
    private final WhitePixel whitePixel;
    private final PipelineRendererConfiguration configuration;

    private RenderOutput renderOutput;

    public DefaultPipelineRenderer(
            PreparedRenderingPipeline preparedRenderingPipeline, PipelineRendererConfiguration configuration) {
        this.configuration = configuration;
        whitePixel = new WhitePixel();

        this.preparedRenderingPipeline = preparedRenderingPipeline;

        preparedRenderingPipeline.initialize(configuration);
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
    public OpenGLContext getRenderContext() {
        return renderContext;
    }

    @Override
    public void render(final RenderOutput renderOutput) {
        // Some platforms are chocking (throwing Exception) when asked to create
        // a Render Buffer with a dimension of 0
        if (renderOutput.getRenderWidth() > 0 && renderOutput.getRenderHeight() > 0) {
            this.renderOutput = renderOutput;
            configuration.startFrame();

            preparedRenderingPipeline.startFrame();

            renderContext.begin();

            // Execute nodes
            RenderPipeline renderPipeline = preparedRenderingPipeline.execute(this);
            renderOutput.output(renderPipeline, this, configuration.getPipelineHelper().getFullScreenRender());
            renderPipeline.destroyDefaultBuffer();
            renderContext.end();

            preparedRenderingPipeline.endFrame();

            configuration.endFrame();
        }
        this.renderOutput = null;
    }

    @Override
    public void dispose() {
        whitePixel.dispose();
        preparedRenderingPipeline.dispose();
    }
}
