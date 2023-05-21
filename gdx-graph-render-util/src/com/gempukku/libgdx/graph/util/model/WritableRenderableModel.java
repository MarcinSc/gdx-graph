package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public interface WritableRenderableModel extends RenderableModel, Disposable {
    WritablePropertyContainer getPropertyContainer();

    void setCullingTest(CullingTest cullingTest);

    void setPosition(Vector3 position);

    void setWorldTransform(Matrix4 worldTransform);
}
