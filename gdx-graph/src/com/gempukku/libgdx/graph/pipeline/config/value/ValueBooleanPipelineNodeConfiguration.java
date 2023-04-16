package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ValueBooleanPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", PipelineFieldType.Boolean));
    }
}
