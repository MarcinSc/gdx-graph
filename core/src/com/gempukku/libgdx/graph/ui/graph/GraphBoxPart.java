package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;


public interface GraphBoxPart extends PropertyBoxPart, Disposable {
    GraphBoxOutputConnector getOutputConnector();

    GraphBoxInputConnector getInputConnector();
}
