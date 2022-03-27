package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.VisTable;

public class ScreenShaderPreviewBoxPart extends VisTable implements GraphBoxPart {
    private final ScreenShaderPreviewWidget shaderPreviewWidget;

    public ScreenShaderPreviewBoxPart() {
        shaderPreviewWidget = new ScreenShaderPreviewWidget(300, 300);

        add(shaderPreviewWidget).grow().row();
    }

    public void initialize(JsonValue data) {
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JsonValue object) {
    }

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
        shaderPreviewWidget.graphChanged(hasErrors, graph);
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}