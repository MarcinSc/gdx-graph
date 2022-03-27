package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.LengthPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class LengthPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public LengthPipelineNodeProducer() {
        super(new LengthPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(PipelineFieldType.Float);
        result.put("output", resultOutput);

        return new SingleInputsPipelineNode(result) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                FieldOutput<?> aFunction = inputs.get("input");
                Object a = aFunction.getValue();

                float resultValue;
                if (a instanceof Float) {
                    resultValue = (Float) a;
                } else if (a instanceof Vector2) {
                    resultValue = ((Vector2) a).len();
                } else if (a instanceof Vector3) {
                    resultValue = ((Vector3) a).len();
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;

                    resultValue = (float) Math.sqrt(
                            aColor.r * aColor.r +
                                    aColor.g * aColor.g +
                                    aColor.b * aColor.b +
                                    aColor.a * aColor.a);
                } else {
                    throw new IllegalArgumentException("Not matching type for function");
                }

                resultOutput.setValue(resultValue);
            }
        };
    }
}
