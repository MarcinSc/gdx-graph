package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;


public interface PropertyDefaultBox {
    Actor getActor();

    JsonValue serializeData();
}
