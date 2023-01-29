package com.gempukku.libgdx.graph.util.patchwork;

public class PatchReference {
    private int indexCount;
    private int vertexCount;
    private int indexStart;
    private int vertexStart;

    public PatchReference(int indexCount, int vertexCount, int indexStart, int vertexStart) {
        this.indexCount = indexCount;
        this.vertexCount = vertexCount;
        this.indexStart = indexStart;
        this.vertexStart = vertexStart;
    }

    public void setIndexStart(int indexStart) {
        this.indexStart = indexStart;
    }

    public void setVertexStart(int vertexStart) {
        this.vertexStart = vertexStart;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getIndexStart() {
        return indexStart;
    }

    public int getVertexStart() {
        return vertexStart;
    }
}
