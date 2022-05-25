package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.gempukku.libgdx.graph.util.FloatBinarySearch;

public class WeightedPositionGenerator implements PositionGenerator {
    private final Array<PositionGenerator> positionGenerators = new Array<>();
    private final FloatArray weights = new FloatArray();
    private float totalWeight = 0;

    public void addPositionGenerator(float weight, PositionGenerator positionGenerator) {
        if (weight <= 0)
            throw new IllegalArgumentException("Weight cannot be less or equal to 0");
        totalWeight += weight;
        weights.add(totalWeight);
        positionGenerators.add(positionGenerator);
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        float u = MathUtils.random() * totalWeight;
        int index = FloatBinarySearch.findLargestLessThanIndex(weights, u);
        return positionGenerators.get(index).generateLocation(location);
    }
}
