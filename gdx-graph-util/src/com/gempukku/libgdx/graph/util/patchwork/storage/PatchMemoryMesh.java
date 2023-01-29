package com.gempukku.libgdx.graph.util.patchwork.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.util.patchwork.PatchReference;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.storage.MemoryMesh;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;
import com.gempukku.libgdx.graph.util.storage.MultiPartMesh;

public class PatchMemoryMesh<T> implements MultiPartMesh<T, PatchReference>, MemoryMesh {
    private final int indexCapacity;
    private final int vertexCapacity;
    private final int vertexSize;
    private final MeshSerializer<T> serializer;

    private final short[] indexValues;
    private final float[] vertexValues;
    private final Array<PatchReference> patches = new Array<>();

    private int nextIndex = 0;
    private int nextVertexIndex = 0;

    private int minUpdatedVertexValueIndex = Integer.MAX_VALUE;
    private int maxUpdatedVertexValueIndex = -1;

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public PatchMemoryMesh(int indexCapacity, int vertexCapacity, MeshSerializer<T> serializer) {
        this.indexCapacity = indexCapacity;
        this.vertexCapacity = vertexCapacity;
        this.vertexSize = serializer.getFloatsPerVertex();
        this.serializer = serializer;
        indexValues = new short[indexCapacity];
        vertexValues = new float[vertexCapacity * vertexSize];
    }

    @Override
    public void setupGdxMesh(Mesh mesh) {
        mesh.setVertices(vertexValues);
        mesh.setIndices(indexValues);
    }

    @Override
    public int getMaxIndexCount() {
        return indexCapacity;
    }

    @Override
    public int getMaxVertexCount() {
        return vertexCapacity;
    }

    @Override
    public boolean isEmpty() {
        return patches.isEmpty();
    }

    @Override
    public boolean canStore(T patch) {
        int indexCount = serializer.getIndexCount(patch);
        int vertexCount = serializer.getVertexCount(patch);
        // Checks if there is a capacity to store it
        if (nextIndex + indexCount > indexValues.length || (vertexCount + nextVertexIndex) * vertexSize > vertexValues.length)
            return false;
        return true;
    }

    @Override
    public PatchReference addPart(T patch) {
        int indexCount = serializer.getIndexCount(patch);
        int vertexCount = serializer.getVertexCount(patch);

        serializer.serializeIndices(patch, indexValues, nextIndex, nextVertexIndex);
        serializer.serializeVertices(patch, vertexValues, nextVertexIndex);

        markIndexesUpdated(nextIndex, indexCount);
        markVertexValuesUpdated(nextVertexIndex, vertexCount);

        PatchReference result = new PatchReference(indexCount, vertexCount, nextIndex, nextVertexIndex);
        patches.add(result);

        nextIndex += indexCount;
        nextVertexIndex += vertexCount;

        return result;
    }

    @Override
    public boolean containsPart(PatchReference partReference) {
        return patches.contains(partReference, true);
    }

    @Override
    public PatchReference updatePart(T patch, PatchReference patchReference) {
        if (patchReference.getVertexCount() != serializer.getVertexCount(patch)
                || patchReference.getIndexCount() != serializer.getIndexCount(patch))
            throw new GdxRuntimeException("Unable to update patch, if number of vertices or indices has changed");

        serializer.serializeIndices(patch, indexValues, patchReference.getIndexStart(), patchReference.getVertexStart());
        serializer.serializeVertices(patch, vertexValues, patchReference.getVertexStart());

        markIndexesUpdated(patchReference.getIndexStart(), patchReference.getIndexCount());
        markVertexValuesUpdated(patchReference.getVertexStart(), patchReference.getVertexCount());

        return patchReference;
    }

    @Override
    public void removePart(PatchReference patchReference) {
        int index = patches.indexOf(patchReference, true);
        patches.removeIndex(index);

        // Need to move all the data in the vertex values
        int valuesPositionStart = patchReference.getVertexStart() * vertexSize;
        int consumedValuesCount = patchReference.getVertexCount() * vertexSize;
        System.arraycopy(vertexValues, valuesPositionStart + consumedValuesCount, vertexValues, valuesPositionStart, nextVertexIndex * vertexSize - valuesPositionStart);
        nextVertexIndex -= patchReference.getVertexCount();
        markVertexValuesUpdated(patchReference.getVertexStart(), nextVertexIndex - patchReference.getVertexStart() - patchReference.getVertexCount());

        // Next, need to move all the indices and also reduce their value by vertex count
        int indexDistance = patchReference.getIndexCount();
        int lastIndexToMove = nextIndex - patchReference.getIndexCount();
        int vertexReduction = patchReference.getVertexCount();
        for (int i = patchReference.getIndexStart(); i < lastIndexToMove; i++) {
            int resultIndex = indexValues[i + indexDistance] - vertexReduction;
            indexValues[i] = (short) resultIndex;
        }
        markIndexesUpdated(patchReference.getIndexStart(), nextIndex - patchReference.getIndexStart() - patchReference.getIndexCount());

        // Decrement the nextIndex and nextVertexIndex
        nextIndex -= patchReference.getIndexCount();
        nextVertexIndex -= patchReference.getVertexCount();

        // Finally notify all the patches that are in the array after the removed one,
        // that their positions have changed
        for (int i = index; i < patches.size; i++) {
            PatchReference pr = patches.get(i);
            pr.setIndexStart(pr.getIndexStart() - patchReference.getIndexCount());
            pr.setVertexStart(pr.getVertexStart() - patchReference.getVertexCount());
        }
    }

    private void markVertexValuesUpdated(int vertexStart, int vertexCount) {
        minUpdatedVertexValueIndex = Math.min(minUpdatedVertexValueIndex, vertexStart * vertexSize);
        maxUpdatedVertexValueIndex = Math.max(maxUpdatedVertexValueIndex, (vertexStart + vertexCount) * vertexSize);
    }

    private void markIndexesUpdated(int indexStart, int indexCount) {
        minUpdatedIndex = Math.min(minUpdatedIndex, indexStart);
        maxUpdatedIndex = Math.max(maxUpdatedIndex, indexStart + indexCount);
    }

    public void clear() {
        nextIndex = 0;
        nextVertexIndex = 0;
        patches.clear();
    }

    public void resetUpdates() {
        minUpdatedVertexValueIndex = Integer.MAX_VALUE;
        maxUpdatedVertexValueIndex = -1;
        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }

    @Override
    public void updateGdxMesh(Mesh mesh) {
        int minUpdatedIndex = this.minUpdatedIndex;
        int maxUpdatedIndex = Math.min(this.maxUpdatedIndex, nextIndex);
        if (minUpdatedIndex < maxUpdatedIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating index array - short count: " + (indexValues.length));
            mesh.setIndices(indexValues);
        }

        int minUpdatedVertexValueIndex = this.minUpdatedVertexValueIndex;
        int maxUpdatedVertexValueIndex = Math.min(this.maxUpdatedVertexValueIndex, nextVertexIndex * vertexSize);
        if (minUpdatedVertexValueIndex < maxUpdatedVertexValueIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating vertex array - float count: " + (maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex));
            mesh.updateVertices(minUpdatedVertexValueIndex, vertexValues, minUpdatedVertexValueIndex, maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex);
        }

        resetUpdates();
    }

    @Override
    public void renderGdxMesh(ShaderProgram shaderProgram, Mesh mesh, int[] attributeLocations, MeshRenderer meshRenderer) {
        int indexStart = 0;
        int indexCount = nextIndex;
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("MeshRendering", "Rendering " + indexCount + " indexes(s)");

        meshRenderer.renderMesh(shaderProgram, mesh, indexStart, indexCount, attributeLocations);
    }
}
