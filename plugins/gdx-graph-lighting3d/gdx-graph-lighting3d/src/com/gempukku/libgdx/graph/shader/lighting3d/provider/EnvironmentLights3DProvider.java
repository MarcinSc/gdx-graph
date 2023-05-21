package com.gempukku.libgdx.graph.shader.lighting3d.provider;


import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.lighting3d.*;

public class EnvironmentLights3DProvider implements Lights3DProvider {
    @Override
    public LightColor getAmbientLight(Lighting3DEnvironment environment, RenderableModel model) {
        return environment != null ? environment.getAmbientColor() : null;
    }

    @Override
    public Array<Directional3DLight> getDirectionalLights(Lighting3DEnvironment environment, RenderableModel model, int count) {
        return environment != null ? environment.getDirectionalLights() : null;
    }

    @Override
    public Array<Point3DLight> getPointLights(Lighting3DEnvironment environment, RenderableModel model, int count) {
        return environment != null ? environment.getPointLights() : null;
    }

    @Override
    public Array<Spot3DLight> getSpotLights(Lighting3DEnvironment environment, RenderableModel model, int count) {
        return environment != null ? environment.getSpotLights() : null;
    }
}
