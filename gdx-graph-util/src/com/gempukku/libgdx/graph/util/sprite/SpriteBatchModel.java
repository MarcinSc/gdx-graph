package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public interface SpriteBatchModel extends Disposable {
    SpriteReference addSprite(RenderableSprite sprite);

    boolean containsSprite(SpriteReference spriteReference);

    SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference);

    void removeSprite(SpriteReference spriteReference);

    int getSpriteCount();

    boolean isAtCapacity();

    WritablePropertyContainer getPropertyContainer();

    void setCullingTest(CullingTest cullingTest);

    void setPosition(Vector3 position);

    void setWorldTransform(Matrix4 worldTransform);
}
