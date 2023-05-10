package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;

public class MultiplyPipelineNodeProducer extends AbstractPipelineNodeProducer {
    public MultiplyPipelineNodeProducer() {
        super(new MultiplyPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final String resultType = outputTypes.get("output");
        final Object resultValue = createDefaultValue(resultType);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(resultType);
        result.put("output", resultOutput);

        return new AbstractPipelineNode(result) {
            private Array<FieldOutput<?>> inputs;

            @Override
            public void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs) {
                this.inputs = inputs.get("inputs");
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                Object returnValue = resetDefaultValue(resultType, resultValue);
                for (FieldOutput<?> input : inputs) {
                    Object value = input.getValue();
                    returnValue = multiply(returnValue, value);
                }

                resultOutput.setValue(returnValue);
            }
        };
    }

    private Object multiply(Object obj, Object value) {
        if (value instanceof Float) {
            float f = (float) value;
            if (obj instanceof Float)
                return (float) obj * f;
            if (obj instanceof Color) {
                return ((Color) obj).mul(f, f, f, f);
            }
            if (obj instanceof Vector2) {
                return ((Vector2) obj).scl(f, f);
            }
            if (obj instanceof Vector3) {
                return ((Vector3) obj).scl(f);
            }
        } else {
            if (obj instanceof Color)
                return ((Color) obj).mul((Color) value);
            if (obj instanceof Vector2)
                return ((Vector2) obj).scl((Vector2) value);
            if (obj instanceof Vector3)
                return ((Vector3) obj).scl((Vector3) value);
        }
        return null;
    }

    private Object createDefaultValue(String type) {
        if (type.equals(PipelineFieldType.Float))
            return 1f;
        else if (type.equals(PipelineFieldType.Vector2))
            return new Vector2(1f, 1f);
        else if (type.equals(PipelineFieldType.Vector3))
            return new Vector3(1f, 1f, 1f);
        else
            return new Color(1, 1, 1, 1);
    }

    private Object resetDefaultValue(String type, Object value) {
        if (type.equals(PipelineFieldType.Float))
            return 1f;
        else if (type.equals(PipelineFieldType.Vector2))
            return ((Vector2) value).set(1f, 1f);
        else if (type.equals(PipelineFieldType.Vector3))
            return ((Vector3) value).set(1f, 1f, 1f);
        else
            return ((Color) value).set(1f, 1f, 1f, 1f);
    }

    private String determineOutputType(Array<PipelineNode.FieldOutput<?>> inputs) {
        String result = PipelineFieldType.Float;
        for (PipelineNode.FieldOutput<?> input : inputs) {
            String fieldType = input.getPropertyType();
            if (!fieldType.equals(result) && (!result.equals(PipelineFieldType.Float) && !fieldType.equals(PipelineFieldType.Float)))
                throw new IllegalStateException("Invalid mix of input field types");
            if (!fieldType.equals(PipelineFieldType.Float))
                result = fieldType;
        }
        return result;
    }
}
