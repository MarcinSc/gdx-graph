package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.DistancePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class DistancePipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public DistancePipelineNodeProducer() {
        super(new DistancePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final String resultType = inputTypes.get("p0");
        final Object resultValue = createResult(resultType);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(resultType);
        result.put("output", resultOutput);

        return new SingleInputsPipelineNode(result) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                FieldOutput<?> aFunction = inputs.get("p0");
                FieldOutput<?> bFunction = inputs.get("p1");

                Object a = aFunction.getValue();
                Object b = bFunction.getValue();

                Object returnValue;
                if (a instanceof Float) {
                    returnValue = Math.abs(((float) a) - ((float) b));
                } else if (a instanceof Vector2) {
                    returnValue = ((Vector2) resultValue).dst((Vector2) b);
                } else if (a instanceof Vector3) {
                    returnValue = ((Vector3) resultValue).dst((Vector3) b);
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;
                    Color bColor = (Color) b;

                    final float p1 = aColor.r - bColor.r;
                    final float p2 = aColor.g - bColor.g;
                    final float p3 = aColor.b - bColor.b;
                    final float p4 = aColor.a - bColor.a;
                    returnValue = (float) Math.sqrt(p1 * p1 + p2 * p2 + p3 * p3 + p4 * p4);
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
