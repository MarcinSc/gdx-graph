package com.gempukku.libgdx.graph.plugin.lighting3d.provider;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.lighting3d.*;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;

public interface Lights3DProvider {
    LightColor getAmbientLight(Lighting3DEnvironment environment, RenderableModel model);

    Array<Directional3DLight> getDirectionalLights(Lighting3DEnvironment environment, RenderableModel model, int count);

    Array<Point3DLight> getPointLights(Lighting3DEnvironment environment, RenderableModel model, int count);

    Array<Spot3DLight> getSpotLights(Lighting3DEnvironment environment, RenderableModel model, int count);
}
