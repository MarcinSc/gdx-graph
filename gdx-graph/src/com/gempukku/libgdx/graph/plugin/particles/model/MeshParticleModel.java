package com.gempukku.libgdx.graph.plugin.particles.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class MeshParticleModel implements ParticleModel {
    private final VertexAttributes vertexAttributes;
    private final float[] meshData;
    private final short[] indexData;
    private final int vertexCount;
    private final int indexCount;

    public MeshParticleModel(Mesh mesh) {
        this.vertexAttributes = copyAttributes(mesh.getVertexAttributes());
        this.vertexCount = mesh.getNumVertices();
        this.indexCount = mesh.getNumIndices();
        meshData = mesh.getVertices(new float[vertexCount * mesh.getVertexSize()]);
        indexData = new short[indexCount];
        mesh.getIndices(indexData);
    }

    private VertexAttributes copyAttributes(VertexAttributes sourceAttributes) {
        Array<VertexAttribute> attributes = new Array<>(VertexAttribute.class);
        for (VertexAttribute vertexAttribute : sourceAttributes) {
            attributes.add(vertexAttribute);
        }
        return new VertexAttributes(attributes.toArray());
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public int getIndexCount() {
        return indexCount;
    }

    @Override
    public void initializeDataBuffer(VertexAttributes vertexAttributes, float[] dataBuffer, int numberOfParticles) {
        int particleLength = vertexAttributes.vertexSize / 4 * vertexCount;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            VertexAttribute meshAttribute = findAttributeInMesh(vertexAttribute);
            if (meshAttribute != null) {
                for (int particleIndex = 0; particleIndex < numberOfParticles; particleIndex++) {
                    int particleOffset = particleIndex * particleLength;
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int sourcePosition = (this.vertexAttributes.vertexSize / 4 * vertexIndex) + (meshAttribute.offset / 4);
                        int destinationPosition = particleOffset + (vertexAttributes.vertexSize / 4 * vertexIndex) + (vertexAttribute.offset / 4);
                        int dataLength = meshAttribute.numComponents;
                        System.arraycopy(meshData, sourcePosition, dataBuffer, destinationPosition, dataLength);
                    }
                }
            }
        }
    }

    private VertexAttribute findAttributeInMesh(VertexAttribute vertexAttribute) {
        for (VertexAttribute attribute : this.vertexAttributes) {
            if (attribute.alias.equals(vertexAttribute.alias))
                return attribute;
        }
        return null;
    }

    @Override
    public void initializeIndexBuffer(short[] indexBuffer, int numberOfParticles) {
        for (int particleIndex = 0; particleIndex < numberOfParticles; particleIndex++) {
            int sourcePosition = 0;
            int destinationPosition = particleIndex * indexCount;
            System.arraycopy(indexData, sourcePosition, indexBuffer, destinationPosition, indexCount);
        }
    }

    @Override
    public void updateParticleData(float[] particlesData, int particleOffset, VertexAttributes vertexAttributes, ObjectMap<String, String> attributeToPropertyMap, ObjectMap<String, PropertySource> properties, ObjectMap<String, Object> attributes) {
        int vertexLength = vertexAttributes.vertexSize / 4;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String propertyName = attributeToPropertyMap.get(vertexAttribute.alias);
            if (propertyName != null) {
                int attributeOffset = vertexAttribute.offset / 4;
                PropertySource propertySource = properties.get(propertyName);
                Object attributeValue = attributes.get(propertyName);
                ParticlesUtil.setParticleAttribute(particlesData, particleOffset, vertexCount, vertexLength,
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
}
