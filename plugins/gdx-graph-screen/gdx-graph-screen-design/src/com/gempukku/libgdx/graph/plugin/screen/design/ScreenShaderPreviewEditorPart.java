package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.ScreenGraphShaderRecipe;
import com.gempukku.libgdx.graph.ui.TabControl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.graph.RequestTabOpen;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

public class ScreenShaderPreviewEditorPart implements GraphNodeEditorPart, GraphChangedAware, Disposable {
    private final ScreenShaderPreview shaderPreviewWidget;
    private final ScreenShaderPreview tabShaderPreviewWidget;

    private final VisTable localTable;
    private final VisTable tabTable;
    private final VisImageButton maximizeButton;

    public ScreenShaderPreviewEditorPart() {
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

        ScreenGraphShaderRecipe screenShaderRecipe = new ScreenGraphShaderRecipe();
        shaderPreviewWidget = new ScreenShaderPreview(screenShaderRecipe);
        tabShaderPreviewWidget = new ScreenShaderPreview(screenShaderRecipe);

        localTable.add(maximizeButton).right().row();
        localTable.add(shaderPreviewWidget).width(300).height(300).row();

        tabTable.add(tabShaderPreviewWidget).grow().row();
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
    public void serializePart(JsonValue object) {
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        if (event.isStructure() || event.isData()) {
            shaderPreviewWidget.graphChanged(hasErrors, graph);
            tabShaderPreviewWidget.graphChanged(hasErrors, graph);
        }
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
        tabShaderPreviewWidget.dispose();
    }
}