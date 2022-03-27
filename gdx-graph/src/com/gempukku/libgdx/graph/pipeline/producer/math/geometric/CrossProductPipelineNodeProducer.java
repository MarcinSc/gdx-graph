package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.CrossProductPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class CrossProductPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public CrossProductPipelineNodeProducer() {
        super(new CrossProductPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final Vector3 tmpVector = new Vector3();

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(PipelineFieldType.Vector3);
        result.put("output", resultOutput);

        return new SingleInputsPipelineNode(result) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                FieldOutput<?> aFunction = inputs.get("a");
                FieldOutput<?> bFunction = inputs.get("b");
                Vector3 a = (Vector3) aFunction.getValue();
                Vector3 b = (Vector3) bFunction.getValue();

                Vector3 returnValue = tmpVector.set(a).crs(b);

                resultOutput.setValue(returnValue);
            }
        };
    }

}
