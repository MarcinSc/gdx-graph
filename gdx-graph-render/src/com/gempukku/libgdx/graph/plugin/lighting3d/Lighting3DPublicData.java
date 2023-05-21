package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.gempukku.libgdx.graph.plugin.lighting3d.provider.Lights3DProvider;

public interface Lighting3DPublicData {
    void setEnvironment(String id, Lighting3DEnvironment environment);

    Lighting3DEnvironment getEnvironment(String id);

    void setLights3DProvider(Lights3DProvider provider);

    Lights3DProvider getLights3DProvider();
}
