package com.gempukku.libgdx.box2d.artemis.shape;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ObjectMap;

public class BoxShapeHandler implements FixtureShapeHandler {
    @Override
    public Shape createShape(Entity entity, ObjectMap<String, String> shapeData, float pixelsToMeters) {
        PolygonShape shape = new PolygonShape();
        float width = Float.parseFloat(shapeData.get("width"));
        float height = Float.parseFloat(shapeData.get("height")) / 2 / pixelsToMeters;
        float centerX = Float.parseFloat(shapeData.get("centerX", "0"));
        float centerY = Float.parseFloat(shapeData.get("centerY", "0"));
        shape.setAsBox(width / 2 / pixelsToMeters, height, new Vector2(centerX / pixelsToMeters, centerY / pixelsToMeters), 0);
        return shape;
    }
}
