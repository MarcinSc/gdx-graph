package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class ModelShaderPreviewBoxPart extends VisTable implements GraphNodeEditorPart, Disposable {
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
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JsonValue value) {

    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        shaderPreviewWidget.graphChanged(hasErrors, graph);
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}