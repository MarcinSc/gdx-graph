package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;

public interface PropertyGraphBoxCustomization {
    void process(DefaultNodeConfiguration configuration, DefaultGraphNodeEditor result, JsonValue data);
}
