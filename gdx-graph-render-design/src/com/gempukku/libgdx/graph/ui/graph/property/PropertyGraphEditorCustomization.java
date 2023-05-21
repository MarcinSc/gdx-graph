package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeConfiguration;

public interface PropertyGraphEditorCustomization {
    void process(DefaultNodeConfiguration configuration, GdxGraphNodeEditor result, JsonValue data);
}
