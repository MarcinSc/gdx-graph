package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;

public class ValueFloatPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueFloatPipelineNodeConfiguration() {
        super("ValueFloat", "Float", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Float));
    }
}
