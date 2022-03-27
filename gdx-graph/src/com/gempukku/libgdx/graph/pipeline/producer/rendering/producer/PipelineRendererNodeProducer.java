package com.gempukku.libgdx.graph.pipeline.producer.rendering.producer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;

public class PipelineRendererNodeProducer extends SingleInputsPipelineNodeProducer {
    public PipelineRendererNodeProducer() {
        super(new PipelineRendererNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private FullScreenRender fullScreenRender;
            private Vector2 tmpVector = new Vector2();

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                fullScreenRender = pipelineDataProvider.getFullScreenRender();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

                RenderPipeline canvasPipeline = (RenderPipeline) inputs.get("input").getValue();
                RenderPipeline paintPipeline = (RenderPipeline) inputs.get("pipeline").getValue();
                Vector2 position = (Vector2) inputs.get("position").getValue();

                final PipelineNode.FieldOutput<Vector2> sizeInput = (PipelineNode.FieldOutput<Vector2>) inputs.get("size");

                RenderPipelineBuffer canvasBuffer = canvasPipeline.getDefaultBuffer();
                RenderPipelineBuffer paintBuffer = paintPipeline.getDefaultBuffer();

                Vector2 size = (sizeInput != null) ? sizeInput.getValue() : tmpVector.set(paintBuffer.getWidth(), paintBuffer.getHeight());

                canvasPipeline.drawTexture(paintBuffer, canvasBuffer, pipelineRenderingContext, fullScreenRender, position.x, position.y, size.x, size.y);

                paintPipeline.destroyDefaultBuffer();

                output.setValue(canvasPipeline);
            }
        };
    }
}
