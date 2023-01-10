package com.gempukku.libgdx.box2d.artemis.shape;

import com.artemis.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ObjectMap;

public interface FixtureShapeHandler {
    Shape createShape(Entity entity, ObjectMap<String, String> shapeData, Matrix4 transform, float pixelsToMeters);
}
