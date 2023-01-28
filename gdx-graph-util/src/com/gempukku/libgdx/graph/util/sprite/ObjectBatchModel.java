package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public interface ObjectBatchModel<T, U> extends Disposable {
    U addObject(T object);

    boolean containsObject(U objectReference);

    U updateObject(T object, U objectReference);

    void removeObject(U objectReference);

    boolean canStore(T object);

    boolean isEmpty();

    WritablePropertyContainer getPropertyContainer();

    void setCullingTest(CullingTest cullingTest);

    void setPosition(Vector3 position);

    void setWorldTransform(Matrix4 worldTransform);
}
