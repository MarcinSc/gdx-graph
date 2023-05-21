package com.gempukku.libgdx.graph.plugin.screen;

public interface GraphScreenShaders {
    /**
     * Sets property on the given screen shader.
     *
     * @param tag   Screen shader tag.
     * @param name  Name of the property.
     * @param value Value of the property.
     */
    void setProperty(String tag, String name, Object value);

    /**
     * Un-sets the property from the given screen shader. If the property is un-set, the default value for that
     * property will be used (specified in the Graph editor).
     *
     * @param tag  Screen shader tag.
     * @param name Name of the property.
     */
    void unsetProperty(String tag, String name);

    /**
     * Returns the value of a property from the given screen shader.
     * Please note - that if the property is not set on the model instance, a null is returned and NOT the default
     * value (from Graph editor).
     *
     * @param tag  Screen shader tag.
     * @param name Name of the property.
     * @return Value of the property.
     */
    Object getProperty(String tag, String name);
}
