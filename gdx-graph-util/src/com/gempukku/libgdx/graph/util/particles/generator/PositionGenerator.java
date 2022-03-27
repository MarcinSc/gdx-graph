package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.Vector3;

public interface PositionGenerator {
    Vector3 generateLocation(Vector3 location);
}
