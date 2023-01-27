package com.gempukku.libgdx.graph.util.sprite.manager;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;

public class MultiPageSpriteBatchModel<T extends SpriteBatchModel> implements SpriteBatchModel {
    private final DisposableProducer<T> spriteBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;

    private final Array<T> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageSpriteBatchModel(DisposableProducer<T> spriteBatchModelProducer) {
        this(spriteBatchModelProducer, new MapWritablePropertyContainer());
    }

    public MultiPageSpriteBatchModel(DisposableProducer<T> spriteBatchModelProducer,
                                     WritablePropertyContainer propertyContainer) {
        this.spriteBatchModelProducer = spriteBatchModelProducer;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (T page : pages) {
            result += page.getSpriteCount();
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
        return getFirstAvailablePage().addSprite(sprite);
    }

    private T getFirstAvailablePage() {
        for (T page : pages) {
            if (!page.isAtCapacity())
                return page;
        }

        T newPage = spriteBatchModelProducer.create();
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        pages.add(newPage);

        return newPage;
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        for (T page : pages) {
            if (page.containsSprite(spriteReference))
                return true;
        }
        return false;
    }

    @Override
    public SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference) {
        for (T page : pages) {
            if (page.containsSprite(spriteReference)) {
                page.updateSprite(sprite, spriteReference);
                return spriteReference;
            }
        }
        throw new GdxRuntimeException("Sprite not found in any of the pages");
    }

    @Override
    public void removeSprite(SpriteReference spriteReference) {
        for (T page : pages) {
            if (page.containsSprite(spriteReference)) {
                page.removeSprite(spriteReference);
                if (page.getSpriteCount() == 0) {
                    spriteBatchModelProducer.dispose(page);
                    pages.removeValue(page, true);
                }
                return;
            }
        }
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    public void disposeOfPage(T page) {
        pages.removeValue(page, true);
        spriteBatchModelProducer.dispose(page);
    }

    @Override
    public void dispose() {
        for (T page : pages) {
            spriteBatchModelProducer.dispose(page);
        }
        spriteBatchModelProducer.dispose();
        pages.clear();
    }

}
