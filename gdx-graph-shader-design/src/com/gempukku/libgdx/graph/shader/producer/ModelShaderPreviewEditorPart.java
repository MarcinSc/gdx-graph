package com.gempukku.libgdx.graph.shader.producer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.builder.ModelGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModel;
import com.gempukku.libgdx.graph.shader.preview.ShaderPreview;
import com.gempukku.libgdx.graph.ui.TabControl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.graph.RequestTabOpen;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.Map;

public class ModelShaderPreviewEditorPart implements GraphNodeEditorPart, GraphChangedAware {
    private final String modelTypeProperty;

    private final ShaderPreview shaderPreview;
    private final VisSelectBox<String> selectBox;

    private final ShaderPreview tabShaderPreview;
    private final VisSelectBox<String> tabSelectBox;

    private final VisTable localTable;
    private final VisTable tabTable;
    private final VisImageButton maximizeButton;

    public ModelShaderPreviewEditorPart(String modelTypeProperty) {
        this.modelTypeProperty = modelTypeProperty;

        localTable = new VisTable();

        tabTable = new VisTable();
        tabTable.setBackground("darkGrey");
        tabTable.setFillParent(true);

        Drawable maximizeDrawable = VisUI.getSkin().getDrawable("icon-maximize");
        maximizeButton = new VisImageButton(maximizeDrawable);
        maximizeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        openInNewTab();
                    }
                });


        Map<String, Supplier<? extends PreviewRenderableModel>> previewModelSuppliers = UIModelShaderConfiguration.getPreviewModelSuppliers();

        Array<String> modelTypes = new Array<>();
        for (String modelType : previewModelSuppliers.keySet()) {
            modelTypes.add(modelType);
        }
        selectBox = new VisSelectBox<>("gdx-graph-property");
        selectBox.setItems(modelTypes);
        tabSelectBox = new VisSelectBox<>("gdx-graph-property");
        tabSelectBox.setItems(modelTypes);

        ModelGraphShaderRecipe modelShaderRecipe = new ModelGraphShaderRecipe();
        shaderPreview = new ShaderPreview(modelShaderRecipe);
        shaderPreview.setRenderableModelSupplier(UIModelShaderConfiguration.getPreviewModelSuppliers().get(selectBox.getSelected()));
        tabShaderPreview = new ShaderPreview(modelShaderRecipe);
        tabShaderPreview.setRenderableModelSupplier(UIModelShaderConfiguration.getPreviewModelSuppliers().get(tabSelectBox.getSelected()));


        ChangeListener modelListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String value = ((VisSelectBox<String>) actor).getSelected();
                setPreviewModel(value);
            }
        };
        selectBox.addListener(modelListener);
        tabSelectBox.addListener(modelListener);

        localTable.add(new VisLabel("Preview model: ", "gdx-graph-property-label"));
        localTable.add(selectBox).growX();
        localTable.add(maximizeButton).row();
        localTable.add(shaderPreview).width(300).height(300).colspan(3).grow().row();

        tabTable.add(new VisLabel("Preview model: ", "gdx-graph-property-label"));
        tabTable.add(tabSelectBox).growX().row();
        tabTable.add(tabShaderPreview).colspan(2).grow().row();
    }

    private void openInNewTab() {
        localTable.fire(
                new RequestTabOpen("preview", "Preview", null,
                        new Supplier<Table>() {
                            @Override
                            public Table get() {
                                return tabTable;
                            }
                        },
                        new Function<TabControl, AssistantPluginTab>() {
                            @Override
                            public AssistantPluginTab evaluate(TabControl tabControl) {
                                return new AssistantPluginTab() {
                                    @Override
                                    public boolean isDirty() {
                                        return false;
                                    }

                                    @Override
                                    public void setActive(boolean b) {

                                    }

                                    @Override
                                    public void closed() {
                                        tabControl.tabClosed(this);
                                    }
                                };
                            }
                        }));
    }

    public void initialize(JsonValue data) {
        String modelTypeProperty = data.getString(this.modelTypeProperty, null);
        if (modelTypeProperty != null) {
            setPreviewModel(modelTypeProperty);
        }
    }

    public void setPreviewModel(String previewModelType) {
        selectBox.setSelected(previewModelType);
        tabSelectBox.setSelected(previewModelType);
        Supplier<? extends PreviewRenderableModel> renderableModelSupplier = UIModelShaderConfiguration.getPreviewModelSuppliers().get(previewModelType);
        shaderPreview.setRenderableModelSupplier(renderableModelSupplier);
        tabShaderPreview.setRenderableModelSupplier(renderableModelSupplier);
    }

    @Override
    public Actor getActor() {
        return localTable;
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
        value.addChild(modelTypeProperty, new JsonValue(selectBox.getSelected()));
    }

    @Override
    public void graphChanged(GraphChangedEvent event, GraphWithProperties graph) {
        if (event.isStructure() || event.isData()) {
            shaderPreview.graphChanged(graph);
            tabShaderPreview.graphChanged(graph);
        }
    }
}