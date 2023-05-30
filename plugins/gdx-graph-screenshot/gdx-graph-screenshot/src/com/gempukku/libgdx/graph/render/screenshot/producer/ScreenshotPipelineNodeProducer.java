package com.gempukku.libgdx.graph.render.screenshot.producer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Screenshot;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class ScreenshotPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ScreenshotPipelineNodeProducer() {
        super(new ScreenshotPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final String path = data.getString("path", null);
        final Function<Float, FileHandle> resultFileFunction = PathParser.parsePath(path);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, configuration) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                if (enabled) {
                    RenderPipelineBuffer defaultBuffer = renderPipeline.getDefaultBuffer();
                    defaultBuffer.beginColor();
                    Screenshot.saveBoundFrameBuffer(resultFileFunction.evaluate(configuration.getTimeProvider().getTime()),
                            0, 0, defaultBuffer.getWidth(), defaultBuffer.getHeight());
                    defaultBuffer.endColor();
                }

                output.setValue(renderPipeline);
            }
        };
    }
}
