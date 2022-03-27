package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;

public interface PropertyBoxPart extends Disposable {
    Actor getActor();

    void initialize(JsonValue object);

    void serializePart(JsonValue object);
}
