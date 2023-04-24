package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class ModelShaderPreviewBoxPart extends VisTable implements GraphNodeEditorPart, GraphChangedAware, Disposable {
    private final ModelShaderPreview shaderPreviewWidget;
    private final VisSelectBox<ModelShaderPreview.ShaderPreviewModel> selectBox;
    private final String modelTypeProperty;

    public ModelShaderPreviewBoxPart(String modelTypeProperty) {
        this.modelTypeProperty = modelTypeProperty;
        shaderPreviewWidget = new ModelShaderPreview(300, 300);
        selectBox = new VisSelectBox<>("gdx-graph-property");
        selectBox.setItems(ModelShaderPreview.ShaderPreviewModel.values());

        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setModel(selectBox.getSelected());
                    }
                });

        add(new VisLabel("Preview model: ", "gdx-graph-property-label"));
        add(selectBox).growX().row();
        add(shaderPreviewWidget).colspan(2).grow().row();
    }

    public void initialize(JsonValue data) {
        String modelTypeProperty = data.getString(this.modelTypeProperty, null);
        if (modelTypeProperty != null) {
            setPreviewModel(ModelShaderPreview.ShaderPreviewModel.valueOf(modelTypeProperty));
        }
    }

    public void setPreviewModel(ModelShaderPreview.ShaderPreviewModel previewModel) {
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
        value.addChild(modelTypeProperty, new JsonValue(selectBox.getSelected().name()));
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        if (event.isStructure() || event.isData()) {
            shaderPreviewWidget.graphChanged(hasErrors, graph);
        }
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}