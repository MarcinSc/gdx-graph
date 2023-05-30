package com.gempukku.libgdx.graph.render.maps.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.maps.MapsRendererConfiguration;

public class MapsRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public MapsRendererPipelineNodeProducer() {
        super(new MapsRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final MapsRendererConfiguration mapsConfiguration = configuration.getConfig(MapsRendererConfiguration.class);
        if (mapsConfiguration == null)
            throw new GdxRuntimeException("A configuration with class MapsRendererConfiguration needs to be define for pipeline");

        final String mapId = data.getString("id");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, configuration) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputs.get("camera");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();
                Camera camera = cameraInput.getValue();
                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                Map map = mapsConfiguration.getMap(mapId);
                MapRenderer mapRenderer = mapsConfiguration.getMapRenderer(mapId);
                if (enabled && map != null) {
                    // Sadly need to switch off (and then on) the RenderContext
                    pipelineRenderingContext.getRenderContext().end();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    currentBuffer.beginColor();
                    mapRenderer.setView((OrthographicCamera) camera);
                    mapRenderer.render();
                    currentBuffer.endColor();

                    pipelineRenderingContext.getRenderContext().begin();
                }

                output.setValue(renderPipeline);
            }
        };
    }
}
