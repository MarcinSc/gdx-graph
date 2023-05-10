package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.NormalizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;

public class NormalizePipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public NormalizePipelineNodeProducer() {
        super(new NormalizePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final String resultType = inputTypes.get("input");
        final Object resultValue = createResult(resultType);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(resultType);
        result.put("output", resultOutput);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private FieldOutput<?> aFunction;

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                aFunction = inputs.get("input");
                Object a = aFunction.getValue();
                Object returnValue;

                if (a instanceof Float) {
                    returnValue = Math.signum((float) a);
                } else if (a instanceof Vector2) {
                    returnValue = ((Vector2) resultValue).set((Vector2) a).nor();
                } else if (a instanceof Vector3) {
                    returnValue = ((Vector3) resultValue).set((Vector3) a).nor();
                } else {
                    Color aColor = (Color) a;

                    float length = (float) Math.sqrt(
                            aColor.r * aColor.r +
                                    aColor.g * aColor.g +
                                    aColor.b * aColor.b +
                                    aColor.a * aColor.a);
                    returnValue = ((Color) resultValue).set(
                            aColor.r / length,
                            aColor.b / length,
                            aColor.b / length,
                            aColor.a / length);
                }

                resultOutput.setValue(returnValue);
            }
        };
    }

    private Object createResult(String returnType) {
        if (returnType.equals(PipelineFieldType.Float)) {
            return 0f;
        } else if (returnType.equals(PipelineFieldType.Vector2)) {
            return new Vector2();
        } else if (returnType.equals(PipelineFieldType.Vector3)) {
            return new Vector3();
        } else if (returnType.equals(PipelineFieldType.Color)) {
            return new Color();
        } else {
            throw new IllegalArgumentException("Not matching type for function");
        }
    }
}
