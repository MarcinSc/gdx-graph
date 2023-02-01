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

public class PropertyShaderNodeBuilder implements GraphShaderNodeBuilder {
    private Array<PropertyShaderCustomization> customizations = new Array<>();

    @Override
    public String getType() {
        return "Property";
    }

    public void addPropertyShaderCustomization(PropertyShaderCustomization customization) {
        customizations.add(customization);
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        PropertyNodeConfiguration propertyNodeConfiguration = new PropertyNodeConfiguration(name, propertyType);
        ShaderFieldType shaderFieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        for (PropertyShaderCustomization customization : customizations) {
            customization.processConfiguration(shaderFieldType, propertyNodeConfiguration);
        }

        return propertyNodeConfiguration;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        ShaderFieldType fieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        ShaderPropertySource shaderPropertySource = graphShaderContext.getPropertySource(name);

        PropertyLocation propertyLocation = shaderPropertySource.getPropertyLocation();
        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (propertyLocation == PropertyLocation.Attribute) {
            result.put("value", fieldType.addAsVertexAttribute(vertexShaderBuilder, data, shaderPropertySource));
        } else if (propertyLocation == PropertyLocation.Uniform) {
            result.put("value", fieldType.addAsLocalUniform(vertexShaderBuilder, data, shaderPropertySource));
        } else {
            result.put("value", fieldType.addAsGlobalUniform(vertexShaderBuilder, data, shaderPropertySource));
        }
        for (PropertyShaderCustomization customization : customizations) {
            customization.processVertexNode(fieldType, propertyLocation, producedOutputs, vertexShaderBuilder, data, shaderPropertySource, result);
        }

        return result;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                                      VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        ShaderFieldType fieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        ShaderPropertySource shaderPropertySource = graphShaderContext.getPropertySource(name);

        PropertyLocation propertyLocation = shaderPropertySource.getPropertyLocation();
        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (propertyLocation == PropertyLocation.Attribute) {
            result.put("value", fieldType.addAsFragmentAttribute(vertexShaderBuilder, fragmentShaderBuilder, data, shaderPropertySource));
        } else if (propertyLocation == PropertyLocation.Uniform) {
            result.put("value", fieldType.addAsLocalUniform(fragmentShaderBuilder, data, shaderPropertySource));
        } else {
            result.put("value", fieldType.addAsGlobalUniform(fragmentShaderBuilder, data, shaderPropertySource));
        }
        for (PropertyShaderCustomization customization : customizations) {
            customization.processFragmentNode(fieldType, propertyLocation, producedOutputs, vertexShaderBuilder, fragmentShaderBuilder, data, shaderPropertySource, result);
        }

        return result;
    }
}
