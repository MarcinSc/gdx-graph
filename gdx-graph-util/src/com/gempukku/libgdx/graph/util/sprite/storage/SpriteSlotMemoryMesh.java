package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.util.Producer;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.storage.MemoryMesh;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;
import com.gempukku.libgdx.graph.util.storage.MultiPartMesh;

public class SpriteSlotMemoryMesh<T, U> implements MultiPartMesh<T, U>, MemoryMesh {
    private final int spriteCapacity;
    private final Producer<U> referenceProducer;
    private final int spriteSize;
    private final MeshSerializer<T> serializer;
    private final short[] indexArray;
    private final float[] vertexValueArray;
    private final Array<U> sprites;
    private final ObjectSet<U> spriteSet = new ObjectSet<>();
    private final int indexCountPerSprite;

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public SpriteSlotMemoryMesh(int spriteCapacity,
                                SpriteModel spriteModel, MeshSerializer<T> serializer,
                                Producer<U> referenceProducer) {
        this.spriteCapacity = spriteCapacity;
        this.referenceProducer = referenceProducer;
        this.spriteSize = spriteModel.getVertexCount() * serializer.getFloatsPerVertex();
        this.indexCountPerSprite = spriteModel.getIndexCount();
        this.vertexValueArray = new float[spriteCapacity * spriteSize];
        this.indexArray = new short[spriteCapacity * indexCountPerSprite];
        this.serializer = serializer;

        spriteModel.initializeIndexBuffer(indexArray, spriteCapacity);

        this.sprites = new Array<>();
    }

    @Override
    public void setupGdxMesh(Mesh mesh) {
        mesh.setVertices(vertexValueArray);
        mesh.setIndices(indexArray);
    }

    @Override
    public int getMaxVertexCount() {
        return spriteCapacity * spriteSize;
    }

    @Override
    public int getMaxIndexCount() {
        return spriteCapacity * indexCountPerSprite;
    }

    @Override
    public boolean isEmpty() {
        return sprites.size == 0;
    }

    @Override
    public boolean canStore(T sprite) {
        return sprites.size < spriteCapacity;
    }

    @Override
    public U addPart(T sprite) {
        if (canStore())
            throw new GdxRuntimeException("Should not attempt to add more sprites, already at capacity");

        int spriteIndex = sprites.size;

        serializer.serializeVertices(sprite, vertexValueArray, spriteIndex * spriteSize);

        U result = referenceProducer.create();
        sprites.add(result);
        spriteSet.add(result);

        markSpriteUpdated(spriteIndex);

        return result;
    }

    @Override
    public boolean containsPart(U spriteReference) {
        return spriteSet.contains(spriteReference);
    }

    @Override
    public U updatePart(T sprite, U spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        serializer.serializeVertices(sprite, vertexValueArray, spriteIndex * spriteSize);

        markSpriteUpdated(spriteIndex);

        return spriteReference;
    }

    private int getSpriteIndex(U spriteReference) {
        return sprites.indexOf(spriteReference, true);
    }

    @Override
    public void removePart(U spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        int spriteCount = sprites.size;

        if (spriteIndex < spriteCount - 1) {
            // Rewire the ids for the last elements to replace the removed one
            sprites.set(spriteIndex, sprites.get(sprites.size - 1));
            // Move the data of the deleted sprite in the array
            System.arraycopy(vertexValueArray, (spriteCount - 1) * spriteSize, vertexValueArray, spriteIndex * spriteSize, spriteSize);

            markSpriteUpdated(spriteIndex);
        } else {
            // There are no more sprites after this one
            sprites.removeIndex(spriteIndex);
        }
        spriteSet.remove(spriteReference);
    }

    public void clear() {
        sprites.clear();
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, spriteIndex * spriteSize);
        maxUpdatedIndex = Math.max(maxUpdatedIndex, (spriteIndex + 1) * spriteSize);
    }

    public int getSpriteCount() {
        return sprites.size;
    }

    public boolean canStore() {
        return getSpriteCount() == spriteCapacity;
    }

    @Override
    public void updateGdxMesh(Mesh mesh) {
        int minUpdatedVertexValueIndex = minUpdatedIndex;
        int maxUpdatedVertexValueIndex = Math.min(maxUpdatedIndex, getSpriteCount() * spriteSize);
        if (minUpdatedVertexValueIndex < maxUpdatedVertexValueIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating vertex array - float count: " + (maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex));
            mesh.updateVertices(minUpdatedVertexValueIndex, vertexValueArray, minUpdatedVertexValueIndex, maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex);
        }

        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }

    @Override
    public void renderGdxMesh(ShaderProgram shaderProgram, Mesh mesh, int[] attributeLocations, MeshRenderer meshRenderer) {
        int indexStart = 0;
        int indexCount = sprites.size * indexCountPerSprite;
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("MeshRendering", "Rendering " + indexCount + " indexes(s)");

        meshRenderer.renderMesh(shaderProgram, mesh, indexStart, indexCount, attributeLocations);
    }
}
