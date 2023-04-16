package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.shader.ShaderContext;

public interface RenderableModel {
    /**
     * Returns the position of the model. This is used for ordering models for rendering which
     * takes distance from camera into account.
     *
     * @return
     */
    Vector3 getPosition();

    /**
     * Checks if this model should be rendered. This method could be used for frustum culling,
     * or as a very quick method for making model not being rendered.
     *
     * @param camera
     * @return
     */
    boolean isRendered(Camera camera);

    /**
     * Should return a world transform to use with the given 'tag' (shader).
     *
     * @return
     */
    Matrix4 getWorldTransform();

    /**
     * Should return properties of the model for a given 'tag' (shader).
     *
     * @return
     */
    PropertyContainer getPropertyContainer();

    void prepareToRender(ShaderContext shaderContext);

    /**
     * Renders the model with the shader. Check for implementations provided to get an
     * idea of what should be done.
     *
     * @param camera
     * @param shaderProgram
     * @param propertyToLocationMapping
     */
    void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping);
}
