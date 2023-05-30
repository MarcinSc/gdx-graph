package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface ShaderInformation {
    ObjectMap<String, BasicShader.Attribute> getShaderAttributes(String tag);
    ObjectMap<String, ShaderPropertySource> getShaderProperties(String tag);
}
