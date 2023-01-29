package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.utils.ObjectMap;

public class DefaultGeneratedPatch implements PatchGenerator.GeneratedPatch {
    private int vertexCount;
    private short[] indexArray;
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public DefaultGeneratedPatch(int vertexCount, short[] indexArray) {
        this.vertexCount = vertexCount;
        this.indexArray = indexArray;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public int getIndexCount() {
        return indexArray.length;
    }

    @Override
    public short[] getPatchIndices() {
        return indexArray;
    }

    @Override
    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}
