package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;

public class ParticleRenderableSprite implements RenderableSprite {
    private float particleBirth;
    private float lifeLength;
    private PropertyContainer propertyContainer;

    public ParticleRenderableSprite(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
        this.particleBirth = particleBirth;
        this.lifeLength = lifeLength;
        this.propertyContainer = propertyContainer;
    }

    public float getParticleBirth() {
        return particleBirth;
    }

    public float getParticleDeath() {
        return particleBirth + lifeLength;
    }

    @Override
    public Vector3 getPosition() {
        return null;
    }

    @Override
    public boolean isRendered(Camera camera) {
        return false;
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex) {
        if (vertexAttribute.alias.equals("a_birthTime"))
            vertexData[startIndex] = getParticleBirth();
        else if (vertexAttribute.alias.equals("a_deathTime"))
            vertexData[startIndex] = particleBirth + lifeLength;
    }
}
