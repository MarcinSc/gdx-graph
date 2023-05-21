package com.gempukku.libgdx.graph.shader.screen;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;

public class FullScreenRenderableModel implements RenderableModel {
    private FullScreenRender fullScreenRender;
    private final PropertyContainer propertyContainer = new MapWritablePropertyContainer();

    public void setFullScreenRender(FullScreenRender fullScreenRender) {
        this.fullScreenRender = fullScreenRender;
    }

    @Override
    public Vector3 getPosition() {
        return null;
    }

    @Override
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return true;
    }

    @Override
    public Matrix4 getWorldTransform() {
        return null;
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {

    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        fullScreenRender.renderFullScreen(shaderProgram);
    }
}
