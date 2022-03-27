package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;

public interface PropertyBox extends GraphProperty, Disposable {
    Actor getActor();

    GraphBox createPropertyBox(Skin skin, String id, float x, float y);
}
