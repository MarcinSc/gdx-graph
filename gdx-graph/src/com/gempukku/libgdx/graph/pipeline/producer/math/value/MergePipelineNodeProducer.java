package com.gempukku.libgdx.graph.pipeline.producer.math.value;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.value.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class MergePipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public MergePipelineNodeProducer() {
        super(new MergePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final Vector2 v2 = new Vector2();
        final Vector3 v3 = new Vector3();
        final Color color = new Color();

        DefaultFieldOutput<Vector2> v2Output = new DefaultFieldOutput<>(PipelineFieldType.Vector2);
        v2Output.setValue(v2);
        DefaultFieldOutput<Vector3> v3Output = new DefaultFieldOutput<>(PipelineFieldType.Vector3);
        v3Output.setValue(v3);
        DefaultFieldOutput<Color> colorOutput = new DefaultFieldOutput<>(PipelineFieldType.Color);
        colorOutput.setValue(color);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        result.put("v2", v2Output);
        result.put("v3", v3Output);
        result.put("color", colorOutput);

        return new SingleInputsPipelineNode(result) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                float xValue = getInputValue("x", 0f);
                float yValue = getInputValue("y", 0f);
                float zValue = getInputValue("z", 0f);
                float wValue = getInputValue("w", 0f);

                v2.set(xValue, yValue);
                v3.set(xValue, yValue, zValue);
                color.set(xValue, yValue, zValue, wValue);
            }
        };
    }
}
