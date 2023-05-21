package com.gempukku.libgdx.graph.plugin.maps;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;

public interface MapsPluginData {
    void setMap(String mapId, Map map, MapRenderer mapRenderer);

    Map getMap(String mapId);

    MapRenderer getMapRenderer(String mapId);
}
