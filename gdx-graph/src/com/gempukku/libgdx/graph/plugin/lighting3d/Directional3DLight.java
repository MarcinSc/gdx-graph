package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;

public class Directional3DLight {
    private final Vector3 direction = new Vector3(0, 0, 0);
    private final LightColor color = new LightColor(1, 1, 1);
    private float intensity = 1f;
    private boolean shadowsEnabled = false;
    private int shadowBufferSize = 256;

    private final OrthographicCamera shadowCamera = new OrthographicCamera();
    private RenderPipelineBuffer shadowFrameBuffer;

    public Directional3DLight() {

    }

    public Directional3DLight(DirectionalLight directionalLight) {
        setDirection(directionalLight.direction);
        setColor(directionalLight.color);
        setIntensity(1f);
    }

    public boolean isShadowsEnabled() {
        return shadowsEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    public int getShadowBufferSize() {
        return shadowBufferSize;
    }

    public void setShadowBufferSize(int shadowBufferSize) {
        this.shadowBufferSize = shadowBufferSize;
    }

    public void updateCamera(Vector3 sceneCenter, float sceneDiameter) {
        shadowCamera.viewportWidth = sceneDiameter;
        shadowCamera.viewportHeight = sceneDiameter;
        shadowCamera.near = 0;
        shadowCamera.far = sceneDiameter;
        // Position the camera based on scene center and half of the diameter away in the negative direction
        shadowCamera.position.set(direction).nor().scl(-sceneDiameter / 2f).add(sceneCenter);
        shadowCamera.direction.set(direction);
        shadowCamera.update(true);
    }

    public OrthographicCamera getShadowCamera() {
        return shadowCamera;
    }

    public RenderPipelineBuffer getShadowFrameBuffer() {
        return shadowFrameBuffer;
    }

    public void setShadowFrameBuffer(RenderPipelineBuffer shadowFrameBuffer) {
        this.shadowFrameBuffer = shadowFrameBuffer;
    }

    public Directional3DLight setDirection(Vector3 direction) {
        return setDirection(direction.x, direction.y, direction.z);
    }

    public Directional3DLight setDirection(float x, float y, float z) {
        direction.set(x, y, z).nor();
        return this;
    }

    public Directional3DLight setColor(Color color) {
        return setColor(color.r, color.g, color.b);
    }

    public Directional3DLight setColor(float red, float green, float blue) {
        color.set(red, green, blue);
        return this;
    }

    public Directional3DLight setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
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
