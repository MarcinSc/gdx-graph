package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class MultiPageObjectBatchModel<T, U> implements ObjectBatchModel<T, U> {
    private final DisposableProducer objectBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;

    private final Array<ObjectBatchModel<T, U>> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageObjectBatchModel(DisposableProducer<? extends ObjectBatchModel<T, U>> objectBatchModelProducer) {
        this(objectBatchModelProducer, new MapWritablePropertyContainer());
    }

    public MultiPageObjectBatchModel(DisposableProducer<? extends ObjectBatchModel<T, U>> objectBatchModelProducer,
                                     WritablePropertyContainer propertyContainer) {
        this.objectBatchModelProducer = objectBatchModelProducer;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public boolean isEmpty() {
        for (ObjectBatchModel<T, U> page : pages) {
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

    private ObjectBatchModel<T, U> getFirstAvailablePage(T object) {
        for (ObjectBatchModel<T, U> page : pages) {
            if (page.canStore(object))
                return page;
        }

        ObjectBatchModel<T, U> newPage = (ObjectBatchModel<T, U>) objectBatchModelProducer.create();
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        pages.add(newPage);

        return newPage;
    }

    @Override
    public boolean containsObject(U objectReference) {
        for (ObjectBatchModel<T, U> page : pages) {
            if (page.containsObject(objectReference))
                return true;
        }
        return false;
    }

    @Override
    public U updateObject(T object, U objectReference) {
        for (ObjectBatchModel<T, U> page : pages) {
            if (page.containsObject(objectReference)) {
                page.updateObject(object, objectReference);
                return objectReference;
            }
        }
        throw new GdxRuntimeException("Object not found in any of the pages");
    }

    @Override
    public void removeObject(U objectReference) {
        for (ObjectBatchModel<T, U> page : pages) {
            if (page.containsObject(objectReference)) {
                page.removeObject(objectReference);
                if (page.isEmpty()) {
                    objectBatchModelProducer.dispose(page);
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

    public void disposeOfPage(ObjectBatchModel<T, U> page) {
        pages.removeValue(page, true);
        objectBatchModelProducer.dispose(page);
    }

    @Override
    public void dispose() {
        for (ObjectBatchModel<T, U> page : pages) {
            objectBatchModelProducer.dispose(page);
        }
        pages.clear();
    }
}
