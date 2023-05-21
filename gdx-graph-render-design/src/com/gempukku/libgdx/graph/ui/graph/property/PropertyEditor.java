package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.GraphProperty;

public interface PropertyEditor extends GraphProperty {
    Actor getActor();
}
