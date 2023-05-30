package com.gempukku.libgdx.graph.util.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.Spot3DLight;

public class Lighting3DEnvironment {
    private final Vector3 sceneCenter = new Vector3();
    private float sceneDiameter;

    private final LightColor ambientColor = new LightColor(Color.WHITE);
    private final Array<Directional3DLight> directionalLights = new Array<>();
    private final Array<Point3DLight> pointLights = new Array<>();
    private final Array<Spot3DLight> spotLights = new Array<>();

    public Lighting3DEnvironment() {
        updateScene(new Vector3(0, 0, 0), 1f);
    }

    public Lighting3DEnvironment(Vector3 sceneCenter, float sceneDiameter) {
        updateScene(sceneCenter, sceneDiameter);
    }

    public void updateScene(Vector3 sceneCenter, float sceneDiameter) {
        this.sceneCenter.set(sceneCenter);
        this.sceneDiameter = sceneDiameter;
    }

    public Vector3 getSceneCenter() {
        return sceneCenter;
    }

    public float getSceneDiameter() {
        return sceneDiameter;
    }

    public LightColor getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Color ambientColor) {
        this.ambientColor.set(ambientColor);
    }

    public void addDirectionalLight(Directional3DLight directionalLight) {
        directionalLights.add(directionalLight);
    }

    public void removeDirectionalLight(Directional3DLight directionalLight) {
        directionalLights.removeValue(directionalLight, true);
    }

    public void addPointLight(Point3DLight pointLight) {
        pointLights.add(pointLight);
    }

    public void removePointLight(Point3DLight pointLight) {
        pointLights.removeValue(pointLight, true);
    }

    public void addSpotLight(Spot3DLight spotLight) {
        spotLights.add(spotLight);
    }

    public void removeSpotLight(Spot3DLight spotLight) {
        spotLights.removeValue(spotLight, true);
    }

    public Array<Directional3DLight> getDirectionalLights() {
        return directionalLights;
    }

    public Array<Point3DLight> getPointLights() {
        return pointLights;
    }

    public Array<Spot3DLight> getSpotLights() {
        return spotLights;
    }
}
