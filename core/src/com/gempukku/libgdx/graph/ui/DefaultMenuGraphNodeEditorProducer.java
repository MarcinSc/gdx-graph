package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditorProducer;

public class DefaultMenuGraphNodeEditorProducer extends DefaultGraphNodeEditorProducer implements MenuGraphNodeEditorProducer {
    private MenuNodeConfiguration configuration;

    public DefaultMenuGraphNodeEditorProducer(MenuNodeConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {

    }
}
