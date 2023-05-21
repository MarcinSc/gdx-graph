package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class GraphModelUtil {
    private GraphModelUtil() {
    }

    public static VertexAttributes getVertexAttributes(ObjectMap<String, BasicShader.Attribute> shaderAttributes) {
        Array<VertexAttribute> vertexAttributeArray = new Array<>(VertexAttribute.class);
        for (ObjectMap.Entry<String, BasicShader.Attribute> shaderAttribute : shaderAttributes) {
            vertexAttributeArray.add(new VertexAttribute(1024, shaderAttribute.value.getComponentCount(), shaderAttribute.key));
        }
        return new VertexAttributes(vertexAttributeArray.toArray());
    }

    public static VertexAttributes getShaderVertexAttributes(GraphModels graphModels, String tag) {
        ObjectMap<String, BasicShader.Attribute> shaderAttributes = graphModels.getShaderAttributes(tag);
        if (shaderAttributes == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        return getVertexAttributes(shaderAttributes);
    }

    public static ObjectMap<VertexAttribute, ShaderPropertySource> getPropertySourceMap(GraphModels graphModels, String tag,
                                                                                        VertexAttributes vertexAttributes) {
        ObjectMap<String, ShaderPropertySource> shaderProperties = graphModels.getShaderProperties(tag);
        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        return getPropertySourceMap(vertexAttributes, shaderProperties);
    }

    public static ObjectMap<VertexAttribute, ShaderPropertySource> getPropertySourceMap(VertexAttributes vertexAttributes,
                                                                                        ObjectMap<String, ShaderPropertySource> shaderProperties) {
        ObjectMap<VertexAttribute, ShaderPropertySource> result = new ObjectMap<>();

        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String alias = vertexAttribute.alias;
            ShaderPropertySource shaderPropertySource = findPropertyByAttributeName(shaderProperties, alias);
            result.put(vertexAttribute, shaderPropertySource);
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

    private static ShaderPropertySource findPropertyByAttributeName(ObjectMap<String, ShaderPropertySource> properties, String attributeName) {
        for (ShaderPropertySource shaderPropertySource : properties.values()) {
            if (shaderPropertySource.isDefiningAttribute(attributeName))
                return shaderPropertySource;
        }
        return null;
    }
}
