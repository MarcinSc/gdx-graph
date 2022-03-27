package com.gempukku.libgdx.graph.plugin.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;

public class UIRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public UIRendererPipelineNodeProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final String stageId = data.getString("id", "");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private UIPluginPrivateData uiPluginData;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                uiPluginData = pipelineDataProvider.getPrivatePluginData(UIPluginPrivateData.class);
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();
                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                Stage stage = uiPluginData.getStage(stageId);
                if (enabled && stage != null) {
                    // Sadly need to switch off (and then on) the RenderContext
                    pipelineRenderingContext.getRenderContext().end();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                    int width = currentBuffer.getWidth();
                    int height = currentBuffer.getHeight();
                    int screenWidth = stage.getViewport().getScreenWidth();
                    int screenHeight = stage.getViewport().getScreenHeight();
                    if (screenWidth != width || screenHeight != height)
                        stage.getViewport().update(width, height, true);

                    currentBuffer.beginColor();
                    stage.draw();
                    currentBuffer.endColor();

                    pipelineRenderingContext.getRenderContext().begin();
                }

                output.setValue(renderPipeline);
            }
        };
    }
}
