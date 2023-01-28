package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public interface ShaderPropertySource {
    String getAttributeName();

    String getAttributeName(int index);

    String getVariableName();

    String getVariableName(int index);

    String getUniformName();

    int getPropertyIndex();

    String getPropertyName();

    ShaderFieldType getShaderFieldType();

    ShaderFieldType getShaderFieldTypeForAttribute(String attributeName);

    PropertyLocation getPropertyLocation();

    Object getValueToUse(Object givenValue);

    Object getValueToUseForAttribute(String attributeName, Object givenValue);

    boolean isDefiningAttribute(String attributeName);
}
