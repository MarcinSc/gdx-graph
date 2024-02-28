package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class ValueVector3PipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ValueVector3PipelineNodeConfiguration() {
        super("ValueVector3", "Vector3", "Constant");
        addNodeOutput(
                new DefaultGraphNodeOutput("value", "Value", Vector3));
    }
}
