package com.gempukku.libgdx.graph.shader.lighting3d;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;

public interface LightingRendererConfiguration<Model> extends RendererConfiguration {
    int getMaxNumberOfDirectionalLights(String environmentId, GraphShader graphShader);

    int getMaxNumberOfPointLights(String environmentId, GraphShader graphShader);

    int getMaxNumberOfSpotlights(String environmentId, GraphShader graphShader);

    float getShadowAcneValue(String environmentId, GraphShader graphShader);

    int getShadowSoftness(String environmentId, GraphShader graphShader);

    LightColor getAmbientLight(String environmentId, GraphShader graphShader, Model model);

    Array<Directional3DLight> getDirectionalLights(String environmentId, GraphShader graphShader, Model model);

    Array<Point3DLight> getPointLights(String environmentId, GraphShader graphShader, Model model);

    Array<Spot3DLight> getSpotLights(String environmentId, GraphShader graphShader, Model model);

    Array<Directional3DLight> getShadowDirectionalLights(String environmentId);

    Vector3 getShadowSceneCenter(String environmentId);

    float getShadowSceneDiameter(String environmentId);
}
