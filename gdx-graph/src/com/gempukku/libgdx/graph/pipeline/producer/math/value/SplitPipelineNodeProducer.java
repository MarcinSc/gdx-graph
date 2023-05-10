package com.gempukku.libgdx.graph.pipeline.producer.math.value;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.value.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;

public class SplitPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public SplitPipelineNodeProducer() {
        super(new SplitPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final DefaultFieldOutput<Float> xOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> yOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> zOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> wOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        result.put("x", xOutput);
        result.put("y", yOutput);
        result.put("z", zOutput);
        result.put("w", wOutput);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                Object input = inputs.get("input").getValue();
                if (input instanceof Vector2) {
                    xOutput.setValue(((Vector2) input).x);
                    yOutput.setValue(((Vector2) input).y);
                } else if (input instanceof Vector3) {
                    xOutput.setValue(((Vector3) input).x);
                    yOutput.setValue(((Vector3) input).y);
                    zOutput.setValue(((Vector3) input).z);
                } else {
                    xOutput.setValue(((Color) input).r);
                    yOutput.setValue(((Color) input).g);
                    zOutput.setValue(((Color) input).b);
                    wOutput.setValue(((Color) input).a);
                }
            }
        };
    }
}
