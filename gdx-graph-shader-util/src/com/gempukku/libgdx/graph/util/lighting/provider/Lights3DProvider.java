package com.gempukku.libgdx.graph.util.lighting.provider;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.Spot3DLight;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;

public interface Lights3DProvider {
    LightColor getAmbientLight(Lighting3DEnvironment environment, RenderableModel model);

    Array<Directional3DLight> getDirectionalLights(Lighting3DEnvironment environment, RenderableModel model, int count);

    Array<Point3DLight> getPointLights(Lighting3DEnvironment environment, RenderableModel model, int count);

    Array<Spot3DLight> getSpotLights(Lighting3DEnvironment environment, RenderableModel model, int count);
}
