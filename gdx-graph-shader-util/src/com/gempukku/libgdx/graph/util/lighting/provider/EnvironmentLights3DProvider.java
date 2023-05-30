package com.gempukku.libgdx.graph.util.lighting.provider;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.Spot3DLight;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;

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
