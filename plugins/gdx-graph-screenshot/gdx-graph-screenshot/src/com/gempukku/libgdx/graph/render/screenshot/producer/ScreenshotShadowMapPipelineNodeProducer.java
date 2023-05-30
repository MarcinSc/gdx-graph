package com.gempukku.libgdx.graph.render.screenshot.producer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
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
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;

public class ScreenshotShadowMapPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ScreenshotShadowMapPipelineNodeProducer() {
        super(new ScreenshotShadowMapPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final String environmentId = data.getString("environmentId", null);
        final int lightIndex = data.getInt("lightIndex", 0);
        final String path = data.getString("path", null);
        final Function<Float, FileHandle> resultFileFunction = PathParser.parsePath(path);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        final LightingRendererConfiguration lightingConfiguration = configuration.getConfig(LightingRendererConfiguration.class);

        return new SingleInputsPipelineNode(result, configuration) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final FieldOutput<Boolean> processorEnabled = (FieldOutput<Boolean>) inputs.get("enabled");
                final FieldOutput<RenderPipeline> renderPipelineInput = (FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                if (enabled) {
                    Array<Directional3DLight> lights = lightingConfiguration.getShadowDirectionalLights(environmentId);
                    Directional3DLight light = lights.get(lightIndex);
                    RenderPipelineBuffer shadowFrameBuffer = light.getShadowFrameBuffer();
                    shadowFrameBuffer.beginColor();
                    Screenshot.saveBoundFrameBuffer(resultFileFunction.evaluate(configuration.getTimeProvider().getTime()),
                            0, 0, shadowFrameBuffer.getWidth(), shadowFrameBuffer.getHeight());
                    shadowFrameBuffer.endColor();
                }

                output.setValue(renderPipeline);
            }
        };
    }
}
