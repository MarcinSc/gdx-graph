package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class GraphNodeEditorProducers implements BiFunction<String, JsonValue, NodeConfiguration> {
    private UIGraphConfiguration[] configurations;

    public GraphNodeEditorProducers(UIGraphConfiguration... configurations) {
        this.configurations = configurations;
    }

    @Override
    public NodeConfiguration evaluate(String type, JsonValue data) {
        return evaluate(type).getConfiguration(data);
    }

    private MenuGraphNodeEditorProducer evaluate(String type) {
        for (UIGraphConfiguration graphConfiguration : configurations) {
            for (MenuGraphNodeEditorProducer graphNodeEditorProducer : graphConfiguration.getGraphNodeEditorProducers()) {
                if (graphNodeEditorProducer.getType().equals(type))
                    return graphNodeEditorProducer;
            }
        }

        throw new GdxRuntimeException("Unable to locate producer for type: " + type);
    }
}
