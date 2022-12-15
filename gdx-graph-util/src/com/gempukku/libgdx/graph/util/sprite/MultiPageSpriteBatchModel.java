package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.manager.SpriteRenderableModelManager;

public class MultiPageSpriteBatchModel<T extends SpriteRenderableModel> implements SpriteBatchModel {
    private final int identifierCountPerPage;
    private final SpriteRenderableModelManager<T> renderableModelManager;
    private final WritablePropertyContainer propertyContainer;

    private final Array<T> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageSpriteBatchModel(SpriteRenderableModelManager<T> renderableModelManager) {
        this(renderableModelManager, new MapWritablePropertyContainer());
    }

    public MultiPageSpriteBatchModel(SpriteRenderableModelManager<T> renderableModelManager,
                                     WritablePropertyContainer propertyContainer) {
        this.identifierCountPerPage = renderableModelManager.getIdentifierCount();
        this.renderableModelManager = renderableModelManager;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (SpriteRenderableModel page : pages) {
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
    public int addSprite(RenderableSprite sprite) {
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            SpriteRenderableModel page = pages.get(pageIndex);
            if (page != null && !page.isAtCapacity()) {
                int spriteIndexOnPage = page.addSprite(sprite);
                return identifierCountPerPage * pageIndex + spriteIndexOnPage;
            }
        }

        T newPage = renderableModelManager.createNewModel(propertyContainer);
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        int spriteIndexOnPage = newPage.addSprite(sprite);
        int pageIndex = findFirstEmptyPageIndex();
        if (pageIndex > -1) {
            pages.set(pageIndex, newPage);
        } else {
            pages.add(newPage);
            pageIndex = pages.size - 1;
        }
        return identifierCountPerPage * pageIndex + spriteIndexOnPage;
    }

    private int findFirstEmptyPageIndex() {
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            SpriteRenderableModel page = pages.get(pageIndex);
            if (page == null)
                return pageIndex;
        }
        return -1;
    }

    @Override
    public int updateSprite(RenderableSprite sprite, int spriteIndex) {
        int pageIndex = spriteIndex / identifierCountPerPage;
        int newSpriteIndex = pages.get(pageIndex).updateSprite(sprite, spriteIndex % identifierCountPerPage);
        return pageIndex * identifierCountPerPage + newSpriteIndex;
    }

    @Override
    public void removeSprite(int spriteIndex) {
        int pageIndex = spriteIndex / identifierCountPerPage;
        T page = pages.get(pageIndex);
        page.removeSprite(spriteIndex % identifierCountPerPage);
        if (page.getSpriteCount() == 0 && renderableModelManager.shouldDisposeEmptyModel(page)) {
            disposePage(page);
            pages.set(pageIndex, null);
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
            disposePage(page);
        }
        pages.clear();
    }

    private void disposePage(T page) {
        renderableModelManager.disposeModel(page);
    }
}
