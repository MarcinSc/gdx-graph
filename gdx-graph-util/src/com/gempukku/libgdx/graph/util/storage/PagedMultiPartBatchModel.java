package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class PagedMultiPartBatchModel<T, U> implements MultiPartBatchModel<T, U> {
    private final DisposableProducer objectBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;

    private final Array<MultiPartBatchModel<T, U>> pages = new Array<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public PagedMultiPartBatchModel(DisposableProducer<? extends MultiPartBatchModel<T, U>> objectBatchModelProducer) {
        this(objectBatchModelProducer, new MapWritablePropertyContainer());
    }

    public PagedMultiPartBatchModel(DisposableProducer<? extends MultiPartBatchModel<T, U>> objectBatchModelProducer,
                                    WritablePropertyContainer propertyContainer) {
        this.objectBatchModelProducer = objectBatchModelProducer;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public boolean isEmpty() {
        for (MultiPartBatchModel<T, U> page : pages) {
            if (!page.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public boolean canStore(T part) {
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
    public U addPart(T object) {
        return getFirstAvailablePage(object).addPart(object);
    }

    private MultiPartBatchModel<T, U> getFirstAvailablePage(T object) {
        for (MultiPartBatchModel<T, U> page : pages) {
            if (page.canStore(object))
                return page;
        }

        MultiPartBatchModel<T, U> newPage = (MultiPartBatchModel<T, U>) objectBatchModelProducer.create();
        newPage.setCullingTest(cullingTest);
        newPage.setPosition(position);
        newPage.setWorldTransform(worldTransform);

        pages.add(newPage);

        return newPage;
    }

    @Override
    public boolean containsPart(U partReference) {
        for (MultiPartBatchModel<T, U> page : pages) {
            if (page.containsPart(partReference))
                return true;
        }
        return false;
    }

    @Override
    public U updatePart(T part, U partReference) {
        for (MultiPartBatchModel<T, U> page : pages) {
            if (page.containsPart(partReference)) {
                page.updatePart(part, partReference);
                return partReference;
            }
        }
        throw new GdxRuntimeException("Object not found in any of the pages");
    }

    @Override
    public void removePart(U partReference) {
        for (MultiPartBatchModel<T, U> page : pages) {
            if (page.containsPart(partReference)) {
                page.removePart(partReference);
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

    public void disposeOfPage(MultiPartBatchModel<T, U> page) {
        pages.removeValue(page, true);
        objectBatchModelProducer.dispose(page);
    }

    @Override
    public void dispose() {
        for (MultiPartBatchModel<T, U> page : pages) {
            objectBatchModelProducer.dispose(page);
        }
        pages.clear();
    }
}
