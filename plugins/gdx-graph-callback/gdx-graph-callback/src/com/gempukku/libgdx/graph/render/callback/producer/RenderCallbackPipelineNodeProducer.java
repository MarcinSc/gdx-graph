package com.gempukku.libgdx.graph.render.callback.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.render.callback.RenderCallback;
import com.gempukku.libgdx.graph.render.callback.RenderCallbackPrivateData;

public class RenderCallbackPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public RenderCallbackPipelineNodeProducer() {
        super(new RenderCallbackPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final String callbackId = data.getString("callbackId", null);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private RenderCallbackPrivateData renderCallbackData;

            @Override
            public void initializePipeline() {
                renderCallbackData = pipelineDataProvider.getPrivatePluginData(RenderCallbackPrivateData.class);
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                if (enabled) {
                    RenderCallback callback = renderCallbackData.getRenderCallback(callbackId);

                    callback.renderCallback(renderPipeline, pipelineDataProvider, pipelineRenderingContext, pipelineRequirementsCallback);
                }

                output.setValue(renderPipeline);
            }
        };
    }
}
