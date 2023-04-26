package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.GraphProperty;

public interface PropertyEditor extends GraphProperty, Disposable {
    Actor getActor();
}
