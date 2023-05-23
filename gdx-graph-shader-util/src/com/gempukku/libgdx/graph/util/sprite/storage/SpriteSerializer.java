package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.util.ValuePerVertex;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;

public class SpriteSerializer implements MeshSerializer<RenderableSprite> {
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;

    private final int floatCountPerVertex;
    private final int vertexCount;
    private final int indexCount;

    public SpriteSerializer(
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            SpriteModel spriteModel) {
        this.vertexAttributes = vertexAttributes;
        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;
        vertexCount = spriteModel.getVertexCount();
        indexCount = spriteModel.getIndexCount();
    }

    @Override
    public int getFloatsPerVertex() {
        return floatCountPerVertex;
    }

    @Override
    public int getIndexCount(RenderableSprite object) {
        return indexCount;
    }

    @Override
    public int getVertexCount(RenderableSprite object) {
        return vertexCount;
    }

    @Override
    public void serializeIndices(RenderableSprite object, short[] indices, int indexStart, int vertexStart) {
        throw new GdxRuntimeException("Unable to serialize indexes for sprites");
    }

    @Override
    public void serializeVertices(RenderableSprite sprite, float[] vertexValues, int vertexStart) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            int attributeOffset = vertexAttribute.offset / 4;

            ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
            String attributeName = vertexAttribute.alias;
            ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
            Object attributeValue = sprite.getValue(shaderPropertySource.getPropertyName());
            if (attributeValue instanceof ValuePerVertex) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = vertexStart + vertexIndex * floatCountPerVertex;

                    Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                    shaderFieldType.setValueInAttributesArray(attributeName, vertexValues, vertexOffset + attributeOffset, shaderPropertySource.getValueToUse(vertexValue));
                }
            } else {
                attributeValue = shaderPropertySource.getValueToUse(attributeValue);
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = vertexStart + vertexIndex * floatCountPerVertex;

                    shaderFieldType.setValueInAttributesArray(attributeName, vertexValues, vertexOffset + attributeOffset, attributeValue);
                }
            }
        }
    }
}
