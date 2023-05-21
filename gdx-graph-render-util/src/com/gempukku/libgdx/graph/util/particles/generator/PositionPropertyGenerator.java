package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.Vector3;

public class PositionPropertyGenerator implements PropertyGenerator {
    private Vector3 tmpVector = new Vector3();

    private PositionGenerator positionGenerator;

    public PositionPropertyGenerator(PositionGenerator positionGenerator) {
        this.positionGenerator = positionGenerator;
    }

    @Override
    public Object generateProperty(float seed) {
        return positionGenerator.generateLocation(tmpVector);
    }
}
