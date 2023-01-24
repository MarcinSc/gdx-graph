package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class PropertyShaderNodeBuilder implements GraphShaderNodeBuilder {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        return new PropertyNodeConfiguration(name, propertyType);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        ShaderFieldType fieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        ShaderPropertySource shaderPropertySource = graphShaderContext.getPropertySource(name);

        PropertyLocation propertyLocation = shaderPropertySource.getPropertyLocation();
        if (propertyLocation == PropertyLocation.Attribute) {
            return LibGDXCollections.singletonMap("value", fieldType.addAsVertexAttribute(vertexShaderBuilder, data, shaderPropertySource));
        } else if (propertyLocation == PropertyLocation.Uniform) {
            return LibGDXCollections.singletonMap("value", fieldType.addAsLocalUniform(vertexShaderBuilder, data, shaderPropertySource));
        } else {
            return LibGDXCollections.singletonMap("value", fieldType.addAsGlobalUniform(vertexShaderBuilder, data, shaderPropertySource));
        }
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                                      VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        ShaderFieldType fieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        ShaderPropertySource shaderPropertySource = graphShaderContext.getPropertySource(name);

        PropertyLocation propertyLocation = shaderPropertySource.getPropertyLocation();
        if (propertyLocation == PropertyLocation.Attribute) {
            return LibGDXCollections.singletonMap("value", fieldType.addAsFragmentAttribute(vertexShaderBuilder, fragmentShaderBuilder, data, shaderPropertySource));
        } else if (propertyLocation == PropertyLocation.Uniform) {
            return LibGDXCollections.singletonMap("value", fieldType.addAsLocalUniform(fragmentShaderBuilder, data, shaderPropertySource));
        } else {
            return LibGDXCollections.singletonMap("value", fieldType.addAsGlobalUniform(fragmentShaderBuilder, data, shaderPropertySource));
        }
    }
}
