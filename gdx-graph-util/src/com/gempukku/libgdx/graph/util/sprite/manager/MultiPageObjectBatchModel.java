package com.gempukku.libgdx.graph.util.sprite.manager;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.ObjectBatchModel;

public class MultiPageObjectBatchModel<T, U, V extends ObjectBatchModel<T, U>> implements ObjectBatchModel<T, U> {
    private final DisposableProducer<V> spriteBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;

    private final Array<V> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageObjectBatchModel(DisposableProducer<V> spriteBatchModelProducer) {
        this(spriteBatchModelProducer, new MapWritablePropertyContainer());
    }

    public MultiPageObjectBatchModel(DisposableProducer<V> spriteBatchModelProducer,
                                     WritablePropertyContainer propertyContainer) {
        this.spriteBatchModelProducer = spriteBatchModelProducer;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public boolean isEmpty() {
        for (V page : pages) {
            if (!page.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public boolean canStore(T object) {
        return true;
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
    public U addObject(T object) {
        return getFirstAvailablePage(object).addObject(object);
    }

    private V getFirstAvailablePage(T sprite) {
        for (V page : pages) {
            if (page.canStore(sprite))
                return page;
        }

        V newPage = spriteBatchModelProducer.create();
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        pages.add(newPage);

        return newPage;
    }

    @Override
    public boolean containsObject(U objectReference) {
        for (V page : pages) {
            if (page.containsObject(objectReference))
                return true;
        }
        return false;
    }

    @Override
    public U updateObject(T object, U objectReference) {
        for (V page : pages) {
            if (page.containsObject(objectReference)) {
                page.updateObject(object, objectReference);
                return objectReference;
            }
        }
        throw new GdxRuntimeException("Sprite not found in any of the pages");
    }

    @Override
    public void removeObject(U objectReference) {
        for (V page : pages) {
            if (page.containsObject(objectReference)) {
                page.removeObject(objectReference);
                if (page.isEmpty()) {
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

    public void disposeOfPage(V page) {
        pages.removeValue(page, true);
        spriteBatchModelProducer.dispose(page);
    }

    @Override
    public void dispose() {
        for (V page : pages) {
            spriteBatchModelProducer.dispose(page);
        }
        pages.clear();
    }
}
