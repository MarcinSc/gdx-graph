package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Spot3DLight {
    private final Vector3 position = new Vector3();
    private final Vector3 direction = new Vector3(0, 0, 0);
    private final LightColor color = new LightColor(1, 1, 1);
    private float intensity = 1f;
    private float cutoffAngle = MathUtils.HALF_PI;
    private float exponent = 1f;

    public Spot3DLight() {

    }

    public Spot3DLight(SpotLight spotLight) {
        setPosition(spotLight.position);
        setDirection(spotLight.direction);
        setColor(spotLight.color);
        setIntensity(spotLight.intensity);
        setCutoffAngle(spotLight.cutoffAngle);
        setExponent(spotLight.exponent);
    }

    public Spot3DLight setPosition(Vector3 position) {
        return setPosition(position.x, position.y, position.z);
    }

    public Spot3DLight setPosition(float x, float y, float z) {
        position.set(x, y, z);
        return this;
    }

    public Spot3DLight setDirection(Vector3 direction) {
        return setDirection(direction.x, direction.y, direction.z);
    }

    public Spot3DLight setDirection(float x, float y, float z) {
        direction.set(x, y, z).nor();
        return this;
    }

    public Spot3DLight setColor(Color color) {
        return setColor(color.r, color.g, color.b);
    }

    public Spot3DLight setColor(float red, float green, float blue) {
        color.set(red, green, blue);
        return this;
    }

    public Spot3DLight setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
    }

    public Spot3DLight setCutoffAngle(float cutoffAngle) {
        this.cutoffAngle = cutoffAngle;
        return this;
    }

    public Spot3DLight setExponent(float exponent) {
        this.exponent = exponent;
        return this;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getCutoffAngle() {
        return cutoffAngle;
    }

    public float getExponent() {
        return exponent;
    }

    public float getDirectionX() {
        return direction.x;
    }

    public float getDirectionY() {
        return direction.y;
    }

    public float getDirectionZ() {
        return direction.z;
    }

    public LightColor getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
}
