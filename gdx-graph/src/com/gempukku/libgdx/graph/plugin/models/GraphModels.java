package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

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
    void addModel(String tag, RenderableModel model);

    /**
     * Removes the provided model from being rendered.
     *
     * @param tag
     * @param model
     */
    void removeModel(String tag, RenderableModel model);

    /**
     * Returns a definition of shader properties for a given 'tag' (shader).
     *
     * @param tag
     * @return
     */
    ObjectMap<String, ShaderPropertySource> getShaderProperties(String tag);

    ObjectMap<String, BasicShader.Attribute> getShaderAttributes(String tag);

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
