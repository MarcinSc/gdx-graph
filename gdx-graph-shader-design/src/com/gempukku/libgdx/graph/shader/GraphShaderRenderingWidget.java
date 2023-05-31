package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.shader.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.shader.producer.DefaultShaderContext;
import com.gempukku.libgdx.ui.DisposableWidget;

public class GraphShaderRenderingWidget extends DisposableWidget {
    private final DefaultShaderContext shaderContext;
    private final StateOpenGLContext renderContext;

    private boolean initialized;
    private FrameBuffer frameBuffer;

    private Camera camera;
    private GraphShader graphShader;
    private PipelineRendererConfiguration pipelineRendererConfiguration;
    private Texture colorTexture;
    private Texture depthTexture;

    public GraphShaderRenderingWidget() {
        shaderContext = new DefaultShaderContext();
        renderContext = new StateOpenGLContext();
    }

    public void setPipelineRendererConfiguration(PipelineRendererConfiguration pipelineRendererConfiguration) {
        this.pipelineRendererConfiguration = pipelineRendererConfiguration;
    }

    public void setColorTexture(Texture texture) {
        this.colorTexture = texture;
    }

    public void setDepthTexture(Texture texture) {
        this.depthTexture = texture;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setGraphShader(GraphShader graphShader) {
        this.graphShader = graphShader;
    }

    @Override
    protected void initializeWidget() {
        initialized = true;
    }

    @Override
    public void act(float delta) {
        validate();
        super.act(delta);
        int width = MathUtils.round(getWidth());
        int height = MathUtils.round(getHeight());
        if (initialized && width > 0 && height > 0) {
            shaderContext.setRenderWidth(width);
            shaderContext.setRenderHeight(height);

            if (hasToRecreateBuffer(width, height)) {
                if (frameBuffer != null) {
                    frameBuffer.dispose();
                }
                frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
            }

            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();

            drawToOffscreen();
        }
    }

    private boolean hasToRecreateBuffer(int width, int height) {
        return frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height;
    }

    private ShaderRendererConfiguration<RenderableModel> getShaderRenderingConfiguration() {
        return pipelineRendererConfiguration.getConfig(ShaderRendererConfiguration.class);
    }

    private void drawToOffscreen() {
        frameBuffer.begin();
        renderContext.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
        if (graphShader != null && pipelineRendererConfiguration != null &&
                colorTexture != null && depthTexture != null &&
                camera != null && hasRenderableModels(getShaderRenderingConfiguration())) {
            ShaderRendererConfiguration shaderRendererConfiguration = getShaderRenderingConfiguration();

            shaderContext.setCamera(camera);
            shaderContext.setColorTexture(colorTexture);
            shaderContext.setDepthTexture(depthTexture);
            shaderContext.setPipelineRendererConfiguration(pipelineRendererConfiguration);
            shaderContext.setGraphShader(graphShader);
            shaderContext.setGlobalPropertyContainer(shaderRendererConfiguration.getGlobalUniforms(graphShader));
            graphShader.begin(shaderContext, renderContext);
            for (Object model : shaderRendererConfiguration.getModels()) {
                if (shaderRendererConfiguration.isRendered(model, graphShader, camera)) {
                    graphShader.render(shaderRendererConfiguration, shaderContext, model);
                }
            }
            graphShader.end();
        }
        renderContext.end();
        frameBuffer.end();
    }

    private boolean hasRenderableModels(ShaderRendererConfiguration shaderRendererConfiguration) {
        for (Object model : getShaderRenderingConfiguration().getModels()) {
            if (shaderRendererConfiguration.isRendered(model, graphShader, camera))
                return true;
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (initialized && graphShader != null && frameBuffer != null) {
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + getHeight(), getWidth(), -getHeight());
        }
    }

    @Override
    protected void disposeWidget() {
        initialized = false;
        if (frameBuffer != null) {
            frameBuffer.dispose();
            frameBuffer = null;
        }
    }
}
