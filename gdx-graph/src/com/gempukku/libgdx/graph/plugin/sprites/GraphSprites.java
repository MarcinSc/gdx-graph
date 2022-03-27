package com.gempukku.libgdx.graph.plugin.sprites;

/**
 * Main interface for operating on sprites.
 */
public interface GraphSprites {
    /**
     * Adds sprite to be rendered using the 'tag' (shader) provided.
     *
     * @param tag
     * @param renderableSprite
     * @return
     */
    GraphSprite addSprite(String tag, RenderableSprite renderableSprite);

    /**
     * Should be called after modifying properties of a sprite. This is necessary to
     * update any information for batched sprites. It is not necessary (though advisable for future
     * proofing) to call this method for sprites that are not batched.
     *
     * @param sprite
     */
    void updateSprite(GraphSprite sprite);

    /**
     * Removes sprite from rendering.
     *
     * @param sprite
     */
    void removeSprite(GraphSprite sprite);

    /**
     * Sets global property for a given 'tag' (shader).
     *
     * @param tag
     * @param name
     * @param value
     */
    void setGlobalProperty(String tag, String name, Object value);

    /**
     * Unsets global property for a given 'tag' (shader), effectively resetting it to the default provided from the
     * designer tool.
     *
     * @param tag
     * @param name
     */
    void unsetGlobalProperty(String tag, String name);

    /**
     * Returns a global property for a given 'tag' (shader).
     *
     * @param tag
     * @param name
     * @return
     */
    Object getGlobalProperty(String tag, String name);
}
