package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;


public class GraphBoxPartImpl implements GraphBoxPart {
    private Actor actor;
    private GraphBoxInputConnector inputConnector;
    private GraphBoxOutputConnector outputConnector;
    private Callback callback;

    public GraphBoxPartImpl(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void setInputConnector(GraphBoxInputConnector.Side side, GraphNodeInput graphNodeInput) {
        inputConnector = new GraphBoxInputConnectorImpl(side, null, graphNodeInput.getFieldId());
    }

    public void setOutputConnector(GraphBoxOutputConnector.Side side, GraphNodeOutput graphNodeOutput) {
        outputConnector = new GraphBoxOutputConnectorImpl(side, null, graphNodeOutput.getFieldId());
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return inputConnector;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return outputConnector;
    }

    @Override
    public void initialize(JsonValue object) {

    }

    @Override
    public void serializePart(JsonValue object) {
        if (callback != null)
            callback.serialize(object);
    }

    @Override
    public void dispose() {

    }

    public interface Callback {
        void serialize(JsonValue object);
    }
}
