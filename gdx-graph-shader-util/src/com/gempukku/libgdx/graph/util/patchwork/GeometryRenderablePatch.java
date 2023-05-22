package com.gempukku.libgdx.graph.util.patchwork;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;

public class GeometryRenderablePatch extends MapWritablePropertyContainer implements RenderablePatch {
    private short[] indicesArray;
    private int vertexCount;

    public GeometryRenderablePatch(short[] indicesArray, int vertexCount) {
        this.indicesArray = indicesArray;
        this.vertexCount = vertexCount;
    }

    @Override
    public int getIndexCount() {
        return indicesArray.length;
    }

    @Override
    public short[] getPatchIndices() {
        return indicesArray;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex) {

    }
}
