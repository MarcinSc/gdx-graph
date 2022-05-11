package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.manager.SpriteRenderableModelManager;

public class MultiPageSpriteBatchModel implements SpriteBatchModel {
    private SpriteRenderableModelManager renderableModelManager;
    private final WritablePropertyContainer propertyContainer;

    private Array<SpriteRenderableModel> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageSpriteBatchModel(SpriteRenderableModelManager<?> renderableModelManager) {
        this(renderableModelManager, new MapWritablePropertyContainer());
    }

    public MultiPageSpriteBatchModel(SpriteRenderableModelManager<?> renderableModelManager,
                                     WritablePropertyContainer propertyContainer) {
        this.renderableModelManager = renderableModelManager;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (SpriteRenderableModel page : pages) {
            result += page.getSpriteCount();
        }

        return result;
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
    public boolean hasSprite(RenderableSprite sprite) {
        for (SpriteRenderableModel page : pages) {
            if (page.hasSprite(sprite))
                return true;
        }

        return false;
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        for (SpriteRenderableModel page : pages) {
            if (page.addSprite(sprite))
                return true;
        }

        SpriteRenderableModel newPage = renderableModelManager.createNewModel(propertyContainer);
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        newPage.addSprite(sprite);
        pages.add(newPage);

        return true;
    }

    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        for (SpriteRenderableModel page : pages) {
            if (page.hasSprite(sprite)) {
                page.updateSprite(sprite);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        for (SpriteRenderableModel page : pages) {
            if (page.removeSprite(sprite)) {
                if (page.getSpriteCount() == 0) {
                    if (renderableModelManager.shouldDisposeEmptyModel(page))
                        disposePage(page);
                }
                return true;
            }
        }
        return false;
    }

    public void disposeEmptyPages() {
        Array.ArrayIterator<SpriteRenderableModel> iterator = pages.iterator();
        while (iterator.hasNext()) {
            SpriteRenderableModel page = iterator.next();
            if (page.getSpriteCount() == 0) {
                disposePage(page);
                iterator.remove();
            }
        }
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void dispose() {
        for (SpriteRenderableModel page : pages) {
            disposePage(page);
        }
        pages.clear();
    }

    private void disposePage(SpriteRenderableModel page) {
        renderableModelManager.disposeModel(page);
    }
}
