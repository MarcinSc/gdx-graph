package com.gempukku.libgdx.graph.pipeline.producer.value.node;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;

public class ValuePipelineNode extends SingleInputsPipelineNode {
    private ObjectMap<String, FieldOutput<?>> result;

    public ValuePipelineNode(NodeConfiguration configuration, String propertyName, final Object value,
                             ObjectMap<String, FieldOutput<?>> result) {
        super(result);

        DefaultFieldOutput valueOutput = new DefaultFieldOutput(configuration.getNodeOutputs().get(propertyName).determineFieldType(null));
        valueOutput.setValue(value);
        result.put(propertyName, valueOutput);
    }

    @Override
    public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

    }
}
