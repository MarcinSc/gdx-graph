package com.gempukku.libgdx.graph.pipeline.producer.provided;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class TimePipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public TimePipelineNodeProducer() {
        super(new TimePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final DefaultFieldOutput<Float> timeFieldOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> sinTimeFieldOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> cosTimeFieldOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> deltaTimeFieldOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        result.put("time", timeFieldOutput);
        result.put("sinTime", sinTimeFieldOutput);
        result.put("cosTime", cosTimeFieldOutput);
        result.put("deltaTime", deltaTimeFieldOutput);

        return new SingleInputsPipelineNode(result) {
            private TimeProvider timeProvider;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                timeProvider = pipelineDataProvider.getTimeProvider();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                float timeValue = timeProvider.getTime();

                timeFieldOutput.setValue(timeValue);
                sinTimeFieldOutput.setValue(MathUtils.sin(timeValue));
                cosTimeFieldOutput.setValue(MathUtils.cos(timeValue));
                deltaTimeFieldOutput.setValue(timeProvider.getDelta());
            }
        };
    }
}
