package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.field.ArrayShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class GraphModelUtil {
    private GraphModelUtil() {
    }

    public static VertexAttributes getVertexAttributes(ObjectMap<String, PropertySource> shaderProperties) {
        Array<VertexAttribute> vertexAttributeArray = new Array<>(VertexAttribute.class);
        for (ObjectMap.Entry<String, PropertySource> shaderProperty : shaderProperties) {
            PropertySource propertySource = shaderProperty.value;
            PropertyLocation propertyLocation = propertySource.getPropertyLocation();
            if (propertyLocation == PropertyLocation.Attribute) {
                ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
                if (shaderFieldType instanceof ArrayShaderFieldType) {
                    int arraySize = ((ArrayShaderFieldType) shaderFieldType).getArrayLength();
                    for (int i = 0; i < arraySize; i++) {
                        vertexAttributeArray.add(new VertexAttribute(1024, shaderFieldType.getNumberOfComponents(), propertySource.getAttributeName(i)));
                    }
                } else {
                    vertexAttributeArray.add(new VertexAttribute(1024, shaderFieldType.getNumberOfComponents(), propertySource.getAttributeName()));
                }
            }
        }

        return new VertexAttributes(vertexAttributeArray.toArray());
    }

    public static VertexAttributes getShaderVertexAttributes(GraphModels graphModels, String tag) {
        ObjectMap<String, PropertySource> shaderProperties = graphModels.getShaderProperties(tag);
        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        return getVertexAttributes(shaderProperties);
    }

    public static ObjectMap<VertexAttribute, PropertySource> getPropertySourceMap(GraphModels graphModels, String tag,
                                                                                  VertexAttributes vertexAttributes) {
        ObjectMap<String, PropertySource> shaderProperties = graphModels.getShaderProperties(tag);
        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        return getPropertySourceMap(vertexAttributes, shaderProperties);
    }

    public static ObjectMap<VertexAttribute, PropertySource> getPropertySourceMap(VertexAttributes vertexAttributes,
                                                                                  ObjectMap<String, PropertySource> shaderProperties) {
        ObjectMap<VertexAttribute, PropertySource> result = new ObjectMap<>();

        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String alias = vertexAttribute.alias;
            PropertySource propertySource = findPropertyByAttributeName(shaderProperties, alias);
            result.put(vertexAttribute, propertySource);
        }
        return result;
    }

    public static int[] getAttributeLocations(ShaderProgram shaderProgram, VertexAttributes vertexAttributes) {
        IntArray resultArray = new IntArray();
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            resultArray.add(shaderProgram.getAttributeLocation(vertexAttribute.alias));
        }
        return resultArray.toArray();
    }

    private static PropertySource findPropertyByAttributeName(ObjectMap<String, PropertySource> properties, String attributeName) {
        for (PropertySource propertySource : properties.values()) {
            if (attributeName.equals(propertySource.getAttributeName()))
                return propertySource;
        }
        return null;
    }
}
