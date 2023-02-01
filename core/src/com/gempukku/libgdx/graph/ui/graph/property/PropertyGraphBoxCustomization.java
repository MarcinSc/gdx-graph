package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;

public interface PropertyGraphBoxCustomization {
    void process(ShaderFieldType shaderFieldType, PropertyNodeConfiguration configuration, GraphBoxImpl result, JsonValue data);
}
