package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.gempukku.libgdx.common.Alignment;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public class RenderOutputs {
    public static final RenderOutput drawToScreen = new DrawToScreenImpl();

    public static RenderOutput createDrawToScreenPart(int x, int y, int width, int height) {
        return new DrawToScreen(x, y, width, height);
    }

    public static RenderOutput createDrawToScreenScaled(Scaling scaling, Alignment alignment, int width, int height) {
        return new DrawToScreenScaled(scaling, alignment, width, height);
    }

    private static class DrawToScreen implements RenderOutput {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public DrawToScreen(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getRenderWidth() {
            return width;
        }

        @Override
        public int getRenderHeight() {
            return height;
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender) {
            renderPipeline.drawTexture(renderPipeline.getDefaultBuffer(), null, pipelineRenderingContext, fullScreenRender,
                    x, y, width, height);
        }
    }

    private static class DrawToScreenScaled implements RenderOutput {
        private final Scaling scaling;
        private final Alignment alignment;
        private final int width;
        private final int height;

        public DrawToScreenScaled(Scaling scaling, Alignment alignment, int width, int height) {
            this.scaling = scaling;
            this.alignment = alignment;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getRenderWidth() {
            return width;
        }

        @Override
        public int getRenderHeight() {
            return height;
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender) {
            RenderPipelineBuffer defaultBuffer = renderPipeline.getDefaultBuffer();

            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();

            Vector2 size = scaling.apply(defaultBuffer.getWidth(), defaultBuffer.getHeight(), screenWidth, screenHeight);
            Vector2 space = alignment.apply(size.x, size.y, screenWidth, screenHeight);

            renderPipeline.drawTexture(defaultBuffer, null, pipelineRenderingContext, fullScreenRender,
                    space.x, space.y, size.x, size.y);
        }
    }

    private static class DrawToScreenImpl implements RenderOutput {
        @Override
        public int getRenderWidth() {
            return Gdx.graphics.getWidth();
        }

        @Override
        public int getRenderHeight() {
            return Gdx.graphics.getHeight();
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender) {
            renderPipeline.drawTexture(renderPipeline.getDefaultBuffer(), null, pipelineRenderingContext, fullScreenRender);
        }
    }
}
