package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.DotProductPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class DotProductPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public DotProductPipelineNodeProducer() {
        super(new DotProductPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final String resultType = inputTypes.get("a");
        final Object resultValue = createResult(resultType);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(resultType);
        result.put("output", resultOutput);

        return new SingleInputsPipelineNode(result) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                FieldOutput<?> aFunction = inputs.get("a");
                FieldOutput<?> bFunction = inputs.get("b");
                Object a = aFunction.getValue();
                Object b = bFunction.getValue();

                Object returnValue;
                if (a instanceof Float) {
                    returnValue = ((float) a) * ((float) b);
                } else if (a instanceof Vector2) {
                    returnValue = ((Vector2) resultValue).dot((Vector2) b);
                } else if (a instanceof Vector3) {
                    returnValue = ((Vector3) resultValue).dot((Vector3) b);
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;
                    Color bColor = (Color) b;

                    returnValue = aColor.r * bColor.r + aColor.g * bColor.g
                            + aColor.b * bColor.b + aColor.a * bColor.a;
                } else {
                    throw new IllegalArgumentException("Not matching type for function");
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
