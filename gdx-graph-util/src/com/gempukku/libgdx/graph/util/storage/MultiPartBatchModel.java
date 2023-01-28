package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public interface MultiPartBatchModel<T, U> extends Disposable {
    U addPart(T object);

    boolean containsPart(U partReference);

    U updatePart(T part, U partReference);

    void removePart(U partReference);

    boolean canStore(T part);

    boolean isEmpty();

    WritablePropertyContainer getPropertyContainer();

    void setCullingTest(CullingTest cullingTest);

    void setPosition(Vector3 position);

    void setWorldTransform(Matrix4 worldTransform);
}
