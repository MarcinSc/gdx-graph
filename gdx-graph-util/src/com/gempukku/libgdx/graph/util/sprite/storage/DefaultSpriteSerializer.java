package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class DefaultSpriteSerializer implements MeshSerializer<RenderableSprite> {
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;

    private final int floatCountPerVertex;
    private final int vertexCount;
    private final int indexCount;

    public DefaultSpriteSerializer(
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            SpriteModel spriteModel) {
        this.vertexAttributes = vertexAttributes;
        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;
        vertexCount = spriteModel.getVertexCount();
        indexCount = spriteModel.getIndexCount();
    }

    @Override
    public int getIndexCount(RenderableSprite object) {
        return indexCount;
    }

    @Override
    public int getFloatCount(RenderableSprite object) {
        return floatCountPerVertex * vertexCount;
    }

    @Override
    public void serializeIndices(RenderableSprite object, short[] indices, int indexStart) {
        throw new GdxRuntimeException("Unable to serialize indexes for sprites");
    }

    @Override
    public void serializeVertices(RenderableSprite sprite, float[] vertexValues, int vertexStart) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            int attributeOffset = vertexAttribute.offset / 4;

            ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
            if (shaderPropertySource == null) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = vertexStart + vertexIndex * floatCountPerVertex;
                    sprite.setUnknownPropertyInAttribute(vertexAttribute, vertexValues, vertexOffset + attributeOffset);
                }
            } else {
                ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
                Object attributeValue = sprite.getValue(shaderPropertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = vertexStart + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(vertexValues, vertexOffset + attributeOffset, shaderPropertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = shaderPropertySource.getValueToUse(attributeValue);
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = vertexStart + vertexIndex * floatCountPerVertex;

                        shaderFieldType.setValueInAttributesArray(vertexValues, vertexOffset + attributeOffset, attributeValue);
                    }
                }
            }
        }
    }
}
