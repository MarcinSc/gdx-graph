package com.gempukku.libgdx.graph.plugin.particles.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class QuadParticleModel implements ParticleModel {
    private static final int VERTEX_COUNT = 4;
    private static final int INDEX_COUNT = 6;

    @Override
    public int getVertexCount() {
        return VERTEX_COUNT;
    }

    @Override
    public int getIndexCount() {
        return INDEX_COUNT;
    }

    @Override
    public void initializeDataBuffer(VertexAttributes vertexAttributes, float[] dataBuffer, int numberOfParticles) {
        VertexAttribute uvAttribute = findVertexAttribute(vertexAttributes, ShaderProgram.TEXCOORD_ATTRIBUTE + 0);
        if (uvAttribute != null) {
            int uvOffset = uvAttribute.offset / 4;
            int numberOfFloatsInVertex = vertexAttributes.vertexSize / 4;

            for (int particle = 0; particle < numberOfParticles; particle++) {
                // Don't need to set UV for first vertex, as it's 0,0
                for (int vertex = 1; vertex < VERTEX_COUNT; vertex++) {
                    int dataIndex = getVertexIndex(particle, vertex, numberOfFloatsInVertex);
                    dataBuffer[dataIndex + uvOffset] = vertex % 2;
                    dataBuffer[dataIndex + uvOffset + 1] = (float) (vertex / 2);
                }
            }
        }
    }

    @Override
    public void initializeIndexBuffer(short[] indexBuffer, int numberOfParticles) {
        int vertexIndex = 0;
        for (int i = 0; i < indexBuffer.length; i += INDEX_COUNT) {
            indexBuffer[i + 0] = (short) (vertexIndex * 4 + 0);
            indexBuffer[i + 1] = (short) (vertexIndex * 4 + 2);
            indexBuffer[i + 2] = (short) (vertexIndex * 4 + 1);
            indexBuffer[i + 3] = (short) (vertexIndex * 4 + 2);
            indexBuffer[i + 4] = (short) (vertexIndex * 4 + 3);
            indexBuffer[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
    }

    @Override
    public void updateParticleData(float[] particlesData, int particleOffset,
                                   VertexAttributes vertexAttributes, ObjectMap<String, String> attributeToPropertyMap,
                                   ObjectMap<String, PropertySource> properties, ObjectMap<String, Object> attributes) {
        int vertexLength = vertexAttributes.vertexSize / 4;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String propertyName = attributeToPropertyMap.get(vertexAttribute.alias);
            if (propertyName != null) {
                int attributeOffset = vertexAttribute.offset / 4;
                PropertySource propertySource = properties.get(propertyName);
                Object attributeValue = attributes.get(propertyName);
                ParticlesUtil.setParticleAttribute(particlesData, particleOffset, VERTEX_COUNT, vertexLength,
                        attributeOffset, propertySource, attributeValue);
            }
        }
    }

    @Override
    public void renderMesh(ShaderProgram shader, Mesh mesh, int[] locations) {
        mesh.bind(shader, locations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, mesh.getMaxIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        mesh.unbind(shader, locations);
    }

    private VertexAttribute findVertexAttribute(VertexAttributes vertexAttributes, String alias) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            if (vertexAttribute.alias.equals(alias))
                return vertexAttribute;
        }

        return null;
    }

    private int getVertexIndex(int particleIndex, int vertexInParticle, int numberOfFloatsInVertex) {
        return ((particleIndex * VERTEX_COUNT) + vertexInParticle) * numberOfFloatsInVertex;
    }
}
