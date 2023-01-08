package com.gempukku.libgdx.graph.artemis.particle;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectComponent extends Component {
    private Array<ParticleEffect> particleEffects = new Array<>();

    public Array<ParticleEffect> getParticleEffects() {
        return particleEffects;
    }
}
