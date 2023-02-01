package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public interface PropertyShaderCustomization {
    void processConfiguration(ShaderFieldType shaderFieldType, PropertyNodeConfiguration propertyNodeConfiguration);

    void processVertexNode(ShaderFieldType fieldType, PropertyLocation propertyLocation, ObjectSet<String> producedOutputs,
                           VertexShaderBuilder vertexShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource,
                           ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> result);

    void processFragmentNode(ShaderFieldType fieldType, PropertyLocation propertyLocation, ObjectSet<String> producedOutputs,
                             VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
                             JsonValue data, ShaderPropertySource shaderPropertySource,
                             ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> result);
}
