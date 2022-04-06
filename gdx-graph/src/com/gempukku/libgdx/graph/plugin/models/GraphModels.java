package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

/**
 * Main interface that is used to operate on models rendered by GraphShaders.
 */
public interface GraphModels {
    /**
     * Adds a provided model to be rendered with the provided 'tag' (shader).
     *
     * @param tag
     * @param model
     * @return
     */
    GraphModel addModel(String tag, RenderableModel model);

    /**
     * Removes the provided model from being rendered.
     *
     * @param model
     */
    void removeModel(GraphModel model);

    /**
     * Returns a definition of shader properties for a given 'tag' (shader).
     *
     * @param tag
     * @return
     */
    ObjectMap<String, PropertySource> getShaderProperties(String tag);

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
