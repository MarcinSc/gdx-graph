package com.gempukku.libgdx.graph.util.patchwork;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.data.PropertyContainer;

public interface RenderablePatch extends PropertyContainer {
    int getIndexCount();

    short[] getPatchIndices();

    int getVertexCount();

    void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex);
}
