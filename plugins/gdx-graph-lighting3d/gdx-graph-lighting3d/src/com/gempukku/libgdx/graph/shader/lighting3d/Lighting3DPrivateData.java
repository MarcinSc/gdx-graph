package com.gempukku.libgdx.graph.shader.lighting3d;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.shader.lighting3d.provider.EnvironmentLights3DProvider;
import com.gempukku.libgdx.graph.shader.lighting3d.provider.Lights3DProvider;

public class Lighting3DPrivateData implements Lighting3DPublicData, RuntimePipelinePlugin {
    private final ObjectMap<String, Lighting3DEnvironment> environments = new ObjectMap<>();
    private Lights3DProvider provider = new EnvironmentLights3DProvider();

    @Override
    public void setEnvironment(String id, Lighting3DEnvironment environment) {
        environments.put(id, environment);
    }

    @Override
    public Lighting3DEnvironment getEnvironment(String id) {
        return environments.get(id);
    }

    @Override
    public void setLights3DProvider(Lights3DProvider provider) {
        this.provider = provider;
    }

    @Override
    public Lights3DProvider getLights3DProvider() {
        return provider;
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }
}
