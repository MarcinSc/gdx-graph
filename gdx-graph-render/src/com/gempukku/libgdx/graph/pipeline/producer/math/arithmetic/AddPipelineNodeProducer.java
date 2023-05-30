package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.AddPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.AbstractPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.AbstractPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class AddPipelineNodeProducer extends AbstractPipelineNodeProducer {
    public AddPipelineNodeProducer() {
        super(new AddPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration pipelineRendererConfiguration) {
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
                    returnValue = add(returnValue, value);
                }

                resultOutput.setValue(returnValue);
            }
        };
    }

    private Object add(Object obj, Object value) {
        if (value instanceof Float) {
            float f = (float) value;
            if (obj instanceof Float)
                return (float) obj + f;
            if (obj instanceof Color) {
                return ((Color) obj).add(f, f, f, f);
            }
            if (obj instanceof Vector2) {
                return ((Vector2) obj).add(f, f);
            }
            if (obj instanceof Vector3) {
                return ((Vector3) obj).add(f);
            }
        } else {
            if (obj instanceof Color)
                return ((Color) obj).add((Color) value);
            if (obj instanceof Vector2)
                return ((Vector2) obj).add((Vector2) value);
            if (obj instanceof Vector3)
                return ((Vector3) obj).add((Vector3) value);
        }
        return null;
    }

    private Object createDefaultValue(String type) {
        if (type.equals(PipelineFieldType.Float))
            return 0f;
        else if (type.equals(PipelineFieldType.Vector2))
            return new Vector2();
        else if (type.equals(PipelineFieldType.Vector3))
            return new Vector3();
        else
            return new Color(0, 0, 0, 0);
    }

    private Object resetDefaultValue(String type, Object value) {
        if (type.equals(PipelineFieldType.Float))
            return 0f;
        else if (type.equals(PipelineFieldType.Vector2))
            return ((Vector2) value).set(0f, 0f);
        else if (type.equals(PipelineFieldType.Vector3))
            return ((Vector3) value).set(0f, 0f, 0f);
        else
            return ((Color) value).set(0f, 0f, 0f, 0f);
    }
}
