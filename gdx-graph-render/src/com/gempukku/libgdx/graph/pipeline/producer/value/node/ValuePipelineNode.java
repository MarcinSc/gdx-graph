package com.gempukku.libgdx.graph.pipeline.producer.value.node;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ValuePipelineNode<T> extends SingleInputsPipelineNode {
    public ValuePipelineNode(NodeConfiguration configuration, String propertyName, final T value,
                             ObjectMap<String, FieldOutput<?>> result,
                             PipelineRendererConfiguration pipelineRendererConfiguration) {
        super(result, pipelineRendererConfiguration);

        DefaultFieldOutput<T> valueOutput = new DefaultFieldOutput<>(configuration.getNodeOutputs().get(propertyName).determineFieldType(null));
        valueOutput.setValue(value);
        result.put(propertyName, valueOutput);
    }

    @Override
    public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

    }
}
