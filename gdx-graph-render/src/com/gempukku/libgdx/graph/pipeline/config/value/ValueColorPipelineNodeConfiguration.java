package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;

public class ValueColorPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Color));
    }
}
