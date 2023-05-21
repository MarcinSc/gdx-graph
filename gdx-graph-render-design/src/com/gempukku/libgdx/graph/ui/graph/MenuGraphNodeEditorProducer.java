package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorProducer;

public interface MenuGraphNodeEditorProducer extends GraphNodeEditorProducer {
    String getMenuLocation();

    String getType();

    NodeConfiguration getConfiguration(JsonValue data);
}
