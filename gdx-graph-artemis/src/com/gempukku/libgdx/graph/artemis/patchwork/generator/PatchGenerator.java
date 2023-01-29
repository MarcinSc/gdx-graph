package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public interface PatchGenerator {
    GeneratedPatch generatePatch(Matrix4 transform, JsonValue generatorParameters);

    interface GeneratedPatch {
        int getIndexCount();

        short[] getPatchIndices();

        int getVertexCount();

        ObjectMap<String, Object> getProperties();
    }
}
