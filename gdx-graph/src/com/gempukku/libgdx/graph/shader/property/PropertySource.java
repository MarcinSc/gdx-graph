package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PropertySource {
    private final int propertyIndex;
    private final String propertyName;
    private final ShaderFieldType shaderFieldType;
    private final PropertyLocation location;
    private final Object defaultValue;
    private final boolean array;

    public PropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType, PropertyLocation location, Object defaultValue) {
        this(propertyIndex, propertyName, shaderFieldType, location, defaultValue, false);
    }

    public PropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType, PropertyLocation location, Object defaultValue,
                          boolean array) {
        this.propertyIndex = propertyIndex;
        this.propertyName = propertyName;
        this.shaderFieldType = shaderFieldType;
        this.location = location;
        this.defaultValue = defaultValue;
        this.array = array;
    }

    public String getAttributeName() {
        if (location == PropertyLocation.Attribute) {
            return "a_property_" + propertyIndex;
        }
        return null;
    }

    public String getAttributeName(int index) {
        if (location == PropertyLocation.Attribute && array) {
            return "a_property_" + propertyIndex + "_" + index;
        }
        return null;
    }

    public String getVariableName() {
        return "v_property_" + propertyIndex;
    }

    public String getVariableName(int index) {
        return "v_property_" + propertyIndex + "_" + index;
    }

    public String getUniformName() {
        return "u_property_" + propertyIndex;
    }

    public boolean isArray() {
        return array;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public ShaderFieldType getShaderFieldType() {
        return shaderFieldType;
    }

    public PropertyLocation getPropertyLocation() {
        return location;
    }

    public Object getValueToUse(Object givenValue) {
        if (!shaderFieldType.accepts(givenValue))
            return defaultValue;
        else
            return shaderFieldType.convert(givenValue);
    }
}
