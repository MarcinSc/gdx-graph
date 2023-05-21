package com.gempukku.libgdx.graph.shader.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;

public class Point3DLight {
    private final Vector3 position = new Vector3(0, 0, 0);
    private final LightColor color = new LightColor(1, 1, 1);
    private float intensity = 1f;

    public Point3DLight() {

    }

    public Point3DLight(PointLight pointLight) {
        setPosition(pointLight.position);
        setColor(pointLight.color);
        setIntensity(pointLight.intensity);
    }

    public Point3DLight setPosition(Vector3 position) {
        return setPosition(position.x, position.y, position.z);
    }

    public Point3DLight setPosition(float x, float y, float z) {
        position.set(x, y, z);
        return this;
    }

    public Point3DLight setColor(Color color) {
        return setColor(color.r, color.g, color.b);
    }

    public Point3DLight setColor(float red, float green, float blue) {
        color.set(red, green, blue);
        return this;
    }

    public Point3DLight setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
    }

    public Vector3 getPosition() {
        return position;
    }

    public LightColor getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
}
