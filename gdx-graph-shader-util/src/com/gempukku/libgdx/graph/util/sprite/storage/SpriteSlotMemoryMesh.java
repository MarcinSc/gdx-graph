package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.storage.*;

public class SpriteSlotMemoryMesh<T> implements MultiPartMesh<T, SpriteReference>, MemoryMesh {
    private final int spriteCapacity;
    private final int spriteSize;
    private final int vertexCountPerSprite;
    private final MeshSerializer<T> serializer;
    private final short[] indexArray;
    private final float[] vertexValueArray;
    private final Array<SpriteReference> sprites;
    private final ObjectSet<SpriteReference> spriteSet = new ObjectSet<>();
    private final int indexCountPerSprite;

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public SpriteSlotMemoryMesh(int spriteCapacity,
                                SpriteModel spriteModel, MeshSerializer<T> serializer) {
        this.spriteCapacity = spriteCapacity;
        this.vertexCountPerSprite = spriteModel.getVertexCount();
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
        return spriteCapacity * vertexCountPerSprite;
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
    public SpriteReference addPart(T sprite) {
        if (canStore())
            throw new GdxRuntimeException("Should not attempt to add more sprites, already at capacity");

        int spriteIndex = sprites.size;

        serializer.serializeVertices(sprite, vertexValueArray, spriteIndex * spriteSize);

        SpriteReference result = new SpriteReference();
        sprites.add(result);
        spriteSet.add(result);

        markSpriteUpdated(spriteIndex);

        return result;
    }

    @Override
    public boolean containsPart(SpriteReference spriteReference) {
        return spriteSet.contains(spriteReference);
    }

    @Override
    public SpriteReference updatePart(T sprite, SpriteReference spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        serializer.serializeVertices(sprite, vertexValueArray, spriteIndex * spriteSize);

        markSpriteUpdated(spriteIndex);

        return spriteReference;
    }

    private int getSpriteIndex(SpriteReference spriteReference) {
        return sprites.indexOf(spriteReference, true);
    }

    @Override
    public void removePart(SpriteReference spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        int spriteCount = sprites.size;

        if (spriteIndex < spriteCount - 1) {
            // Rewire the ids for the last elements to replace the removed one
            sprites.set(spriteIndex, sprites.removeIndex(sprites.size - 1));
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
    public void updateGdxMesh(MeshUpdater meshUpdater) {
        int minUpdatedVertexValueIndex = minUpdatedIndex;
        int maxUpdatedVertexValueIndex = Math.min(maxUpdatedIndex, getSpriteCount() * spriteSize);
        if (minUpdatedVertexValueIndex < maxUpdatedVertexValueIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating vertex array - float count: " + (maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex));
            meshUpdater.updateMeshValues(vertexValueArray, minUpdatedVertexValueIndex, maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex);
        }

        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }

    @Override
    public void renderGdxMesh(IndexedMeshRenderer meshRenderer) {
        int indexStart = 0;
        int indexCount = sprites.size * indexCountPerSprite;
        if (indexCount > 0) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Rendering " + indexCount + " indexes(s)");
            meshRenderer.renderMesh(indexStart, indexCount);
        }
    }
}
