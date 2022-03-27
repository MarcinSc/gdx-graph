package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;


public interface GraphBoxProducer {
    String getType();

    boolean isCloseable();

    String getName();

    String getMenuLocation();

    GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data);

    GraphBox createDefault(Skin skin, String id);
}
