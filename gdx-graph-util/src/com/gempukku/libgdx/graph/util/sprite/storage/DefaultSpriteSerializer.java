package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class DefaultSpriteSerializer implements SpriteSerializer<RenderableSprite> {
    private VertexAttributes vertexAttributes;
    private ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;

    private int floatCountPerVertex;
    private int vertexCount;

    public DefaultSpriteSerializer(VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
                                   SpriteModel spriteModel) {
        this.vertexAttributes = vertexAttributes;
        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;
        vertexCount = spriteModel.getVertexCount();
    }

    @Override
    public int getFloatCount() {
        return floatCountPerVertex * vertexCount;
    }

    @Override
    public void serializeToFloatArray(RenderableSprite sprite, float[] vertexData, int startIndex) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            int attributeOffset = vertexAttribute.offset / 4;

            ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
            if (shaderPropertySource == null) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = startIndex + vertexIndex * floatCountPerVertex;
                    sprite.setUnknownPropertyInAttribute(vertexAttribute, vertexData, vertexOffset + attributeOffset);
                }
            } else {
                ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
                Object attributeValue = sprite.getValue(shaderPropertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = startIndex + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, shaderPropertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = shaderPropertySource.getValueToUse(attributeValue);
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = startIndex + vertexIndex * floatCountPerVertex;

                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, attributeValue);
                    }
                }
            }
        }
    }

}
