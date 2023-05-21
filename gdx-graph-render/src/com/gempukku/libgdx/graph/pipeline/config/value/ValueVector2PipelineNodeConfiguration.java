package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class ValueVector2PipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueVector2PipelineNodeConfiguration() {
        super("ValueVector2", "Vector2", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Vector2));
    }
}
