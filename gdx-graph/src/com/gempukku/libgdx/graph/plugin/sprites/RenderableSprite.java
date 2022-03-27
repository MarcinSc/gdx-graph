package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableSprite {
    /**
     * Returns the position of the sprite. This is used for ordering sprites for rendering which
     * takes sprite distance from camera into account.
     *
     * @return
     */
    Vector3 getPosition();

    /**
     * Checks if this sprite should be rendered. Note that if sprites are batched, the result of
     * this method will be ignored and the sprite will be rendered. This method is mostly used for
     * culling in cases where sprites are rendered individually.
     *
     * @param camera
     * @return
     */
    boolean isRendered(Camera camera);

    /**
     * Should return properties of the sprite for a given 'tag' (shader).
     *
     * @param tag
     * @return
     */
    PropertyContainer getPropertyContainer(String tag);
}
