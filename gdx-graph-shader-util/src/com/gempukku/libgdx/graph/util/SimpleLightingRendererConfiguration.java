package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.lighting3d.*;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.util.lighting.provider.EnvironmentLights3DProvider;
import com.gempukku.libgdx.graph.util.lighting.provider.Lights3DProvider;

public class SimpleLightingRendererConfiguration implements LightingRendererConfiguration<RenderableModel> {
    private final int maxDirectionalLights;
    private final int maxPointLights;
    private final int maxSpotLights;
    private final float shadowAcneValue;
    private final int shadowSoftness;
    private final Lights3DProvider lights3DProvider;
    private final ObjectMap<String, Lighting3DEnvironment> environments = new ObjectMap<>();

    private Array<Directional3DLight> tmpDirectionalLightArray = new Array<>();

    public SimpleLightingRendererConfiguration() {
        this(5, 2, 2, 0.01f, 1);
    }

    public SimpleLightingRendererConfiguration(int maxDirectionalLights, int maxPointLights, int maxSpotLights, float shadowAcneValue, int shadowSoftness) {
        this(maxDirectionalLights, maxPointLights, maxSpotLights, shadowAcneValue, shadowSoftness, new EnvironmentLights3DProvider());
    }

    public SimpleLightingRendererConfiguration(
            int maxDirectionalLights, int maxPointLights, int maxSpotLights,
            float shadowAcneValue, int shadowSoftness,
            Lights3DProvider lights3DProvider) {
        this.maxDirectionalLights = maxDirectionalLights;
        this.maxPointLights = maxPointLights;
        this.maxSpotLights = maxSpotLights;
        this.shadowAcneValue = shadowAcneValue;
        this.shadowSoftness = shadowSoftness;
        this.lights3DProvider = lights3DProvider;
    }

    public void setEnvironment(String environmentId, Lighting3DEnvironment environment) {
        environments.put(environmentId, environment);
    }

    public Lighting3DEnvironment getEnvironment(String environmentId) {
        return environments.get(environmentId);
    }

    public void removeEnvironment(String environmentId) {
        environments.remove(environmentId);
    }

    @Override
    public int getMaxNumberOfDirectionalLights(String environmentId, GraphShader graphShader) {
        return maxDirectionalLights;
    }

    @Override
    public int getMaxNumberOfPointLights(String environmentId, GraphShader graphShader) {
        return maxPointLights;
    }

    @Override
    public int getMaxNumberOfSpotlights(String environmentId, GraphShader graphShader) {
        return maxSpotLights;
    }

    @Override
    public float getShadowAcneValue(String environmentId, GraphShader graphShader) {
        return shadowAcneValue;
    }

    @Override
    public int getShadowSoftness(String environmentId, GraphShader graphShader) {
        return shadowSoftness;
    }

    @Override
    public LightColor getAmbientLight(String environmentId, GraphShader graphShader, RenderableModel renderableModel) {
        return lights3DProvider.getAmbientLight(getEnvironment(environmentId), renderableModel);
    }

    @Override
    public Array<Directional3DLight> getDirectionalLights(String environmentId, GraphShader graphShader, RenderableModel renderableModel) {
        return lights3DProvider.getDirectionalLights(getEnvironment(environmentId), renderableModel, maxDirectionalLights);
    }

    @Override
    public Array<Point3DLight> getPointLights(String environmentId, GraphShader graphShader, RenderableModel renderableModel) {
        return lights3DProvider.getPointLights(getEnvironment(environmentId), renderableModel, maxPointLights);
    }

    @Override
    public Array<Spot3DLight> getSpotLights(String environmentId, GraphShader graphShader, RenderableModel renderableModel) {
        return lights3DProvider.getSpotLights(getEnvironment(environmentId), renderableModel, maxSpotLights);
    }

    @Override
    public Array<Directional3DLight> getShadowDirectionalLights(String environmentId) {
        Lighting3DEnvironment environment = getEnvironment(environmentId);
        tmpDirectionalLightArray.clear();
        for (Directional3DLight directionalLight : environment.getDirectionalLights()) {
            if (directionalLight.isShadowsEnabled()) {
                tmpDirectionalLightArray.add(directionalLight);
            }
        }
        return tmpDirectionalLightArray;
    }

    @Override
    public Vector3 getShadowSceneCenter(String environmentId) {
        return getEnvironment(environmentId).getSceneCenter();
    }

    @Override
    public float getShadowSceneDiameter(String environmentId) {
        return getEnvironment(environmentId).getSceneDiameter();
    }
}
