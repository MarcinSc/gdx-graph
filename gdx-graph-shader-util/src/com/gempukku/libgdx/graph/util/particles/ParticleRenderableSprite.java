package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;

public class ParticleRenderableSprite implements RenderableSprite {
    private final float particleBirth;
    private final float lifeLength;
    private final ObjectSet<String> particleBirthProperties;
    private final ObjectSet<String> particleDeathProperties;
    private final PropertyContainer propertyContainer;

    public ParticleRenderableSprite(
            float particleBirth, float lifeLength,
            ObjectSet<String> particleBirthProperties, ObjectSet<String> particleDeathProperties,
            PropertyContainer propertyContainer) {
        this.particleBirth = particleBirth;
        this.lifeLength = lifeLength;
        this.particleBirthProperties = particleBirthProperties;
        this.particleDeathProperties = particleDeathProperties;
        this.propertyContainer = propertyContainer;
    }

    public float getParticleBirth() {
        return particleBirth;
    }

    public float getLifeLength() {
        return lifeLength;
    }

    public float getParticleDeath() {
        return particleBirth + lifeLength;
    }

    @Override
    public Object getValue(String name) {
        if (particleBirthProperties.contains(name))
            return particleBirth;
        else if (particleDeathProperties.contains(name))
            return particleBirth + lifeLength;
        else
            return propertyContainer.getValue(name);
    }
}
