package com.gempukku.libgdx.graph.plugin.particles.impl;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.particles.ParticleUpdater;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.plugin.particles.model.ParticleModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class ParticlesDataContainer implements Disposable {
    private final ParticleUpdaterCallback particleUpdaterCallback = new ParticleUpdaterCallback();

    private Object[] particleDataStorage;
    private float[] particlesData;
    private Mesh mesh;

    private final VertexAttributes vertexAttributes;
    private final ParticleModel particleModel;
    private final ObjectMap<String, PropertySource> properties;
    private final int numberOfParticles;
    private final int numberOfFloatsInVertex;
    private int nextParticleIndex = 0;
    private float maxParticleDeath;

    private int firstDirtyParticle = Integer.MAX_VALUE;
    private int lastDirtyParticle = -1;

    private int birthTimeOffset = -1;
    private int deathTimeOffset = -1;

    private final ObjectMap<String, String> attributeToPropertyMap = new ObjectMap<>();

    public ParticlesDataContainer(VertexAttributes vertexAttributes,
                                  ParticleModel particleModel,
                                  ObjectMap<String, PropertySource> properties,
                                  int numberOfParticles, boolean storeParticleData) {
        this.vertexAttributes = vertexAttributes;
        this.particleModel = particleModel;
        this.properties = properties;
        this.numberOfParticles = numberOfParticles;
        this.numberOfFloatsInVertex = vertexAttributes.vertexSize / 4;

        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String alias = vertexAttribute.alias;
            if (alias.equalsIgnoreCase("a_birthTime"))
                birthTimeOffset = vertexAttribute.offset / 4;
            else if (alias.equalsIgnoreCase("a_deathTime"))
                deathTimeOffset = vertexAttribute.offset / 4;
            else if (alias.startsWith("a_property_")) {
                int propertyIndex = Integer.parseInt(alias.substring(11));
                attributeToPropertyMap.put(alias, getPropertyNameByIndex(propertyIndex));
            }
        }
        initBuffers(vertexAttributes);
        if (storeParticleData)
            particleDataStorage = new Object[numberOfParticles];
    }

    private String getPropertyNameByIndex(int propertyIndex) {
        for (PropertySource property : properties.values()) {
            if (property.getPropertyIndex() == propertyIndex)
                return property.getPropertyName();
        }
        return null;
    }

    private void initBuffers(VertexAttributes vertexAttributes) {
        int numberOfVertices = numberOfParticles * particleModel.getVertexCount();
        int numberOfIndices = numberOfParticles * particleModel.getIndexCount();

        mesh = new Mesh(false, true, numberOfVertices, numberOfIndices, vertexAttributes);

        int dataLength = numberOfVertices * numberOfFloatsInVertex;

        particlesData = new float[dataLength];
        particleModel.initializeDataBuffer(vertexAttributes, particlesData, numberOfParticles);
        mesh.setVertices(particlesData);

        short[] indices = new short[numberOfIndices];
        particleModel.initializeIndexBuffer(indices, numberOfParticles);
        mesh.setIndices(indices);
    }

    private int getVertexIndex(int particleIndex, int vertexInParticle) {
        return ((particleIndex * particleModel.getVertexCount()) + vertexInParticle) * numberOfFloatsInVertex;
    }

    public int getNumberOfParticles() {
        return numberOfParticles;
    }

    public void generateParticle(float particleTime, float lifeLength, Object particleData, ObjectMap<String, Object> attributes) {
        if (particleDataStorage != null)
            particleDataStorage[nextParticleIndex] = particleData;

        float particleBirth = particleTime;
        float particleDeath = particleTime + lifeLength;
        for (int i = 0; i < particleModel.getVertexCount(); i++) {
            int vertexIndex = getVertexIndex(nextParticleIndex, i);
            if (birthTimeOffset != -1)
                particlesData[vertexIndex + birthTimeOffset] = particleBirth;
            if (deathTimeOffset != -1)
                particlesData[vertexIndex + deathTimeOffset] = particleDeath;
        }

        particleModel.updateParticleData(particlesData, getVertexIndex(nextParticleIndex, 0),
                vertexAttributes, attributeToPropertyMap,
                properties, attributes);

        maxParticleDeath = Math.max(maxParticleDeath, particleDeath);

        firstDirtyParticle = Math.min(firstDirtyParticle, nextParticleIndex);
        lastDirtyParticle = Math.max(lastDirtyParticle, nextParticleIndex);

        nextParticleIndex = (nextParticleIndex + 1) % numberOfParticles;
    }

    public void applyPendingChanges() {
        if (lastDirtyParticle >= 0) {
            if (firstDirtyParticle == lastDirtyParticle) {
                // Update all particles
                mesh.updateVertices(0, particlesData, 0, particlesData.length);
            } else if (firstDirtyParticle > lastDirtyParticle) {
                // Updates are wrapper around
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                mesh.updateVertices(firstData, particlesData, firstData, particlesData.length - firstData);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                mesh.updateVertices(0, particlesData, 0, lastData);
            } else {
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                mesh.updateVertices(firstData, particlesData, firstData, lastData - firstData);
            }
        }
    }

    public void render(ParticlesGraphShader graphShader, ShaderContext shaderContext, float currentTime) {
        if (currentTime < maxParticleDeath) {
            graphShader.renderParticles(shaderContext, particleModel, mesh);
        }
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }

    public void update(ParticleUpdater particleUpdater, float currentTime, boolean accessToData) {
        for (int i = 0; i < numberOfParticles; i++) {
            int particleDataIndex = getVertexIndex(i, 0);
            if (currentTime < particlesData[particleDataIndex + deathTimeOffset]) {
                Object particleData = null;
                if (accessToData && particleDataStorage != null)
                    particleData = particleDataStorage[i];

                particleUpdaterCallback.setParticleIndex(i);
                particleUpdater.updateParticle(particleData, particleUpdaterCallback);

                firstDirtyParticle = Math.min(firstDirtyParticle, i);
                lastDirtyParticle = Math.max(lastDirtyParticle, i);
            }
        }
    }

    private class ParticleUpdaterCallback implements ParticleUpdater.ParticleUpdateCallback {
        private int particleIndex;

        public void setParticleIndex(int particleIndex) {
            this.particleIndex = particleIndex;
        }

        @Override
        public void updateParticle(ObjectMap<String, Object> attributes) {
            particleModel.updateParticleData(particlesData, getVertexIndex(particleIndex, 0),
                    vertexAttributes, attributeToPropertyMap,
                    properties, attributes);
        }
    }
}
