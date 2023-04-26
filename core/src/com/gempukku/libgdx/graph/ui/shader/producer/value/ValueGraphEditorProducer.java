package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;

public abstract class ValueGraphEditorProducer implements MenuGraphNodeEditorProducer {
    protected MenuNodeConfiguration configuration;

    public ValueGraphEditorProducer(MenuNodeConfiguration configuration) {
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
    public String getName() {
        return configuration.getName();
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public GraphNodeEditor createNodeEditor(JsonValue data) {
        GdxGraphNodeEditor graphNodeEditor = new GdxGraphNodeEditor(configuration);
        graphNodeEditor.addGraphEditorPart(createValuePart());

        if (data != null)
            graphNodeEditor.initialize(data);

        return graphNodeEditor;
    }

    protected abstract DefaultGraphNodeEditorPart createValuePart();
}
