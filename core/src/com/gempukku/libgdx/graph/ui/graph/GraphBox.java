package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;

import java.util.Map;

public interface GraphBox extends GraphNode, Disposable {
    Actor getActor();

    Map<String, GraphBoxInputConnector> getInputs();

    Map<String, GraphBoxOutputConnector> getOutputs();

    void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph);
}
