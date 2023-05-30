package com.gempukku.libgdx.graph.render.maps;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;

public interface MapsRendererConfiguration extends RendererConfiguration {
    Map getMap(String mapId);

    MapRenderer getMapRenderer(String mapId);
}
