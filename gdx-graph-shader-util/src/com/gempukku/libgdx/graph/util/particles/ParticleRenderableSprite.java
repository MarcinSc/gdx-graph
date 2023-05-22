package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.data.PropertyContainer;
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
    public Object getValue(String name) {
        return propertyContainer.getValue(name);
    }

    @Override
    public void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex) {
        if (vertexAttribute.alias.equals("a_birthTime"))
            vertexData[startIndex] = getParticleBirth();
        else if (vertexAttribute.alias.equals("a_deathTime"))
            vertexData[startIndex] = particleBirth + lifeLength;
    }
}
