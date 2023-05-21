package com.gempukku.libgdx.graph.render.maps;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;

public class MapsPluginPrivateData implements MapsPluginData, RuntimePipelinePlugin {
    private final ObjectMap<String, Map> maps = new ObjectMap<>();
    private final ObjectMap<String, MapRenderer> mapRenderers = new ObjectMap<>();

    @Override
    public void setMap(String mapId, Map map, MapRenderer mapRenderer) {
        maps.put(mapId, map);
        mapRenderers.put(mapId, mapRenderer);
    }

    @Override
    public Map getMap(String mapId) {
        return maps.get(mapId);
    }

    @Override
    public MapRenderer getMapRenderer(String mapId) {
        return mapRenderers.get(mapId);
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }
}
