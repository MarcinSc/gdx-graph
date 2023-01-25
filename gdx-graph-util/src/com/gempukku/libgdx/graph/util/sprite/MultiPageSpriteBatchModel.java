package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.manager.SpriteBatchModelManager;

public class MultiPageSpriteBatchModel<T extends SpriteBatchModel> implements SpriteBatchModel {
    private final SpriteBatchModelManager<T> spriteBatchModelManager;
    private final WritablePropertyContainer propertyContainer;

    private final Array<T> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageSpriteBatchModel(SpriteBatchModelManager<T> spriteBatchModelManager) {
        this(spriteBatchModelManager, new MapWritablePropertyContainer());
    }

    public MultiPageSpriteBatchModel(SpriteBatchModelManager<T> spriteBatchModelManager,
                                     WritablePropertyContainer propertyContainer) {
        this.spriteBatchModelManager = spriteBatchModelManager;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (T page : pages) {
            if (page != null) {
                result += page.getSpriteCount();
            }
        }

        return result;
    }

    @Override
    public boolean isAtCapacity() {
        return false;
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    @Override
    public SpriteReference addSprite(RenderableSprite sprite) {
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            T page = pages.get(pageIndex);
            if (page != null && !page.isAtCapacity()) {
                return page.addSprite(sprite);
            }
        }

        T newPage = spriteBatchModelManager.createNewModel(propertyContainer);
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        SpriteReference spriteReference = newPage.addSprite(sprite);
        int pageIndex = findFirstEmptyPageIndex();
        if (pageIndex > -1) {
            pages.set(pageIndex, newPage);
        } else {
            pages.add(newPage);
        }
        return spriteReference;
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        for (T page : pages) {
            if (page != null && page.containsSprite(spriteReference))
                return true;
        }
        return false;
    }

    private int findFirstEmptyPageIndex() {
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            T page = pages.get(pageIndex);
            if (page == null)
                return pageIndex;
        }
        return -1;
    }

    @Override
    public SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference) {
        for (T page : pages) {
            if (page != null && page.containsSprite(spriteReference)) {
                page.updateSprite(sprite, spriteReference);
                return spriteReference;
            }
        }
        throw new GdxRuntimeException("Sprite not found in any of the pages");
    }

    @Override
    public void removeSprite(SpriteReference spriteReference) {
        for (int i = 0; i < pages.size; i++) {
            T page = pages.get(i);
            if (page != null && page.containsSprite(spriteReference)) {
                page.removeSprite(spriteReference);
                if (page.getSpriteCount() == 0 && spriteBatchModelManager.shouldDisposeEmptyModel(page)) {
                    disposePage(page);
                    pages.set(i, null);
                }
                return;
            }
        }
    }

    public void disposeEmptyPages() {
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            T page = pages.get(pageIndex);
            if (page != null && page.getSpriteCount() == 0) {
                disposePage(page);
                pages.set(pageIndex, null);
            }
        }
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void dispose() {
        for (T page : pages) {
            if (page != null) {
                disposePage(page);
            }
        }
        pages.clear();
    }

    private void disposePage(T page) {
        spriteBatchModelManager.disposeModel(page);
    }
}
