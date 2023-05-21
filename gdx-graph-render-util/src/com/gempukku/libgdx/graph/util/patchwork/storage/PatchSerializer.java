package com.gempukku.libgdx.graph.util.patchwork.storage;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.patchwork.RenderablePatch;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;

public class PatchSerializer implements MeshSerializer<RenderablePatch> {
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;

    private final int floatCountPerVertex;

    public PatchSerializer(VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources) {
        this.vertexAttributes = vertexAttributes;
        this.vertexPropertySources = vertexPropertySources;

        this.floatCountPerVertex = vertexAttributes.vertexSize / 4;
    }

    @Override
    public int getFloatsPerVertex() {
        return floatCountPerVertex;
    }

    @Override
    public int getIndexCount(RenderablePatch object) {
        return object.getIndexCount();
    }

    @Override
    public int getVertexCount(RenderablePatch object) {
        return object.getVertexCount() * floatCountPerVertex;
    }

    @Override
    public void serializeIndices(RenderablePatch patch, short[] indices, int indexStart, int vertexStart) {
        short[] patchIndices = patch.getPatchIndices();
        for (int index = 0, size = patch.getIndexCount(); index < size; index++) {
            indices[indexStart + index] = (short) (vertexStart + patchIndices[index]);
        }
    }

    @Override
    public void serializeVertices(RenderablePatch patch, float[] vertexValues, int vertexStart) {
        int vertexCount = patch.getVertexCount();

        int patchDataStart = vertexStart * floatCountPerVertex;

        // Populate the vertexData with data from patch
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            int attributeOffset = vertexAttribute.offset / 4;

            ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
            if (shaderPropertySource == null) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = patchDataStart + vertexIndex * floatCountPerVertex;
                    patch.setUnknownPropertyInAttribute(vertexAttribute, vertexValues, vertexOffset + attributeOffset);
                }
            } else {
                String attributeName = vertexAttribute.alias;
                ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
                Object attributeValue = patch.getValue(shaderPropertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = patchDataStart + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(attributeName, vertexValues, vertexOffset + attributeOffset, shaderPropertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = shaderPropertySource.getValueToUse(attributeValue);
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = patchDataStart + vertexIndex * floatCountPerVertex;

                        shaderFieldType.setValueInAttributesArray(attributeName, vertexValues, vertexOffset + attributeOffset, attributeValue);
                    }
                }
            }
        }
    }
}
