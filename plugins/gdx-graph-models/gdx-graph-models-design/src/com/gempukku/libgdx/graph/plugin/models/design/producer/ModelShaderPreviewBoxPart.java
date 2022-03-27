package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class ModelShaderPreviewBoxPart extends VisTable implements GraphBoxPart {
    private final ModelShaderPreviewWidget shaderPreviewWidget;
    private final VisSelectBox<ModelShaderPreviewWidget.ShaderPreviewModel> selectBox;

    public ModelShaderPreviewBoxPart() {
        shaderPreviewWidget = new ModelShaderPreviewWidget(300, 300);
        selectBox = new VisSelectBox<ModelShaderPreviewWidget.ShaderPreviewModel>();
        selectBox.setItems(ModelShaderPreviewWidget.ShaderPreviewModel.values());

        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setModel(selectBox.getSelected());
                    }
                });

        add("Preview model: ");
        add(selectBox).growX().row();
        add(shaderPreviewWidget).colspan(2).grow().row();
    }

    public void initialize(JsonValue data) {
    }

    public void setPreviewModel(ModelShaderPreviewWidget.ShaderPreviewModel previewModel) {
        shaderPreviewWidget.setModel(previewModel);
        selectBox.setSelected(previewModel);
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