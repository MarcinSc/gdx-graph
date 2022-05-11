package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public interface SpriteBatchModel extends Disposable {
    boolean addSprite(RenderableSprite sprite);

    boolean hasSprite(RenderableSprite sprite);

    boolean removeSprite(RenderableSprite sprite);

    boolean updateSprite(RenderableSprite sprite);

    int getSpriteCount();

    WritablePropertyContainer getPropertyContainer();

    void setCullingTest(CullingTest cullingTest);

    void setPosition(Vector3 position);

    void setWorldTransform(Matrix4 worldTransform);
}
