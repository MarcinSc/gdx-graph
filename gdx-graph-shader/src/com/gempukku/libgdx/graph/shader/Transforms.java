package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class Transforms implements TransformUpdate {
    private final Array<TransformUpdate> updates = new Array();

    private Transforms() {
    }

    @Override
    public void updateTransform(Matrix4 transform) {
        for (TransformUpdate update : updates) {
            update.updateTransform(transform);
        }
    }

    public Transforms transform(TransformUpdate update) {
        updates.add(update);
        return this;
    }

    public static Transforms create() {
        return new Transforms();
    }

    public Transforms idt() {
        transform(
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.idt();
                    }
                });
        return this;
    }

    public Transforms translate(final float x, final float y, final float z) {
        transform(
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.translate(x, y, z);
                    }
                });
        return this;
    }

    public Transforms rotate(final float axisX, final float axisY, final float axisZ, final float degrees) {
        transform(
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.rotate(axisX, axisY, axisZ, degrees);
                    }
                });
        return this;
    }

    public Transforms scale(final float scaleX, final float scaleY, final float scaleZ) {
        transform(
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.scale(scaleX, scaleY, scaleZ);
                    }
                });
        return this;
    }
}
