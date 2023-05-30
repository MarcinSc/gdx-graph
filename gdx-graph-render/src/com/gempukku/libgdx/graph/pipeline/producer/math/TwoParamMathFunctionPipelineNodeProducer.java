package com.gempukku.libgdx.graph.pipeline.producer.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public abstract class TwoParamMathFunctionPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private final String param1;
    private final String param2;
    private final String outputName;

    public TwoParamMathFunctionPipelineNodeProducer(NodeConfiguration configuration) {
        this(configuration, "a", "b", "output");
    }

    public TwoParamMathFunctionPipelineNodeProducer(NodeConfiguration configuration,
                                                    String param1, String param2, String outputName) {
        super(configuration);
        this.param1 = param1;
        this.param2 = param2;
        this.outputName = outputName;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final String resultType = inputTypes.get(param1);
        final Object resultValue = createResult(resultType);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput resultOutput = new DefaultFieldOutput(resultType);
        result.put(outputName, resultOutput);

        return new SingleInputsPipelineNode(result, configuration) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                FieldOutput<?> aFunction = inputs.get(param1);
                FieldOutput<?> bFunction = inputs.get(param2);
                Object a = aFunction.getValue();
                Object b = bFunction.getValue();

                Object returnValue;
                if (resultType.equals(PipelineFieldType.Float)) {
                    returnValue = executeFunction(a, b, 0);
                } else if (resultType.equals(PipelineFieldType.Vector2)) {
                    returnValue = ((Vector2) resultValue).set(
                            executeFunction(a, b, 0),
                            executeFunction(a, b, 1));
                } else if (resultType.equals(PipelineFieldType.Vector3)) {
                    returnValue = ((Vector3) resultValue).set(
                            executeFunction(a, b, 0),
                            executeFunction(a, b, 1),
                            executeFunction(a, b, 2)
                    );
                } else {
                    returnValue = ((Color) resultValue).set(
                            executeFunction(a, b, 0),
                            executeFunction(a, b, 1),
                            executeFunction(a, b, 2),
                            executeFunction(a, b, 3));
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

    private float executeFunction(Object a, Object b, int index) {
        return executeFunction(getParamValue(a, index), getParamValue(b, index));
    }

    private float getParamValue(Object value, int index) {
        if (value instanceof Float) {
            return (float) value;
        } else if (value instanceof Vector2) {
            Vector2 v2 = (Vector2) value;
            return index == 0 ? v2.x : v2.y;
        } else if (value instanceof Vector3) {
            Vector3 v3 = (Vector3) value;
            return index == 0 ? v3.x : (index == 1 ? v3.y : v3.z);
        } else if (value instanceof Color) {
            Color c = (Color) value;
            return index == 0 ? c.r : (index == 1 ? c.g : (index == 2 ? c.b : c.a));
        }
        throw new IllegalArgumentException("Unknown type of value");
    }

    protected abstract float executeFunction(float a, float b);
}
