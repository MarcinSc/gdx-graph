package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisTable;

public class ScreenShaderPreviewBoxPart extends VisTable implements GraphNodeEditorPart, Disposable {
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
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JsonValue object) {
    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        shaderPreviewWidget.graphChanged(hasErrors, graph);
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}