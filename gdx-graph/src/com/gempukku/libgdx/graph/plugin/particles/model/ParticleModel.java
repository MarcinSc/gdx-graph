package com.gempukku.libgdx.graph.plugin.particles.model;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public interface ParticleModel {
    int getVertexCount();

    int getIndexCount();

    void initializeDataBuffer(VertexAttributes vertexAttributes, float[] dataBuffer, int numberOfParticles);

    void initializeIndexBuffer(short[] indexBuffer, int numberOfParticles);

    void updateParticleData(float[] particlesData, int particleOffset, VertexAttributes vertexAttributes,
                            ObjectMap<String, String> attributeToPropertyMap, ObjectMap<String, PropertySource> properties, ObjectMap<String, Object> attributes);

    void renderMesh(ShaderProgram shader, Mesh mesh, int[] locations);
}
