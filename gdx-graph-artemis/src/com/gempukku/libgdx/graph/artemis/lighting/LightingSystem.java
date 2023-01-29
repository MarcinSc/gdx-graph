package com.gempukku.libgdx.graph.artemis.lighting;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;

public class LightingSystem extends BaseSystem {
    private PipelineRendererSystem pipelineRendererSystem;

    private ComponentMapper<LightingEnvironmentComponent> environmentComponentMapper;
    private ComponentMapper<DirectionalLightComponent> directionalLightComponentMapper;

    private ObjectMap<String, Lighting3DEnvironment> environmentMap = new ObjectMap<>();
    private IntMap<Directional3DLight> directionalLightMap = new IntMap<>();

    private Array<Entity> newEnvironments = new Array<>();
    private Array<Entity> newDirectionalLights = new Array<>();

    @Override
    protected void initialize() {
        world.getAspectSubscriptionManager().get(Aspect.all(LightingEnvironmentComponent.class)).
                addSubscriptionListener(
                        new EntitySubscription.SubscriptionListener() {
                            @Override
                            public void inserted(IntBag entities) {
                                for (int i = 0, size = entities.size(); i < size; i++) {
                                    newEnvironments.add(world.getEntity(entities.get(i)));
                                }
                            }

                            @Override
                            public void removed(IntBag entities) {
                                for (int i = 0, size = entities.size(); i < size; i++) {
                                    LightingEnvironmentComponent environment = environmentComponentMapper.get(entities.get(i));
                                    String environmentName = environment.getName();
                                    environmentMap.remove(environmentName);
                                    Lighting3DPublicData pluginData = pipelineRendererSystem.getPluginData(Lighting3DPublicData.class);
                                    pluginData.setEnvironment(environment.getId(), null);
                                }
                            }
                        });
        world.getAspectSubscriptionManager().get(Aspect.all(DirectionalLightComponent.class)).
                addSubscriptionListener(
                        new EntitySubscription.SubscriptionListener() {
                            @Override
                            public void inserted(IntBag entities) {
                                for (int i = 0, size = entities.size(); i < size; i++) {
                                    newDirectionalLights.add(world.getEntity(entities.get(i)));
                                }
                            }

                            @Override
                            public void removed(IntBag entities) {
                                for (int i = 0, size = entities.size(); i < size; i++) {
                                    int entityId = entities.get(i);
                                    DirectionalLightComponent light = directionalLightComponentMapper.get(entityId);
                                    String environmentName = light.getEnvironmentName();
                                    Lighting3DEnvironment environment = environmentMap.get(environmentName);
                                    environment.removeDirectionalLight(directionalLightMap.remove(entityId));
                                }
                            }
                        }
                );
    }

    @Override
    protected void processSystem() {
        for (Entity newEnvironment : newEnvironments) {
            LightingEnvironmentComponent environment = environmentComponentMapper.get(newEnvironment);
            Lighting3DEnvironment lighting = new Lighting3DEnvironment(environment.getCenter(), environment.getRadius());
            lighting.setAmbientColor(environment.getAmbientColor());
            pipelineRendererSystem.getPluginData(Lighting3DPublicData.class).setEnvironment(environment.getId(), lighting);
            environmentMap.put(environment.getName(), lighting);
        }
        newEnvironments.clear();
        for (Entity newDirectionalLight : newDirectionalLights) {
            DirectionalLightComponent directionalLight = directionalLightComponentMapper.get(newDirectionalLight);
            Lighting3DEnvironment environment = environmentMap.get(directionalLight.getEnvironmentName());
            Directional3DLight light = new Directional3DLight();
            light.setDirection(directionalLight.getDirection());
            light.setColor(directionalLight.getColor());
            light.setIntensity(directionalLight.getIntensity());
            light.setShadowsEnabled(directionalLight.isShadowEnabled());
            light.setShadowBufferSize(directionalLight.getShadowBufferSize());
            environment.addDirectionalLight(light);
            directionalLightMap.put(newDirectionalLight.getId(), light);
        }
        newDirectionalLights.clear();
    }
}
