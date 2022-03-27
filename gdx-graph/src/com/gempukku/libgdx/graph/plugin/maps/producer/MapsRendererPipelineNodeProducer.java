package com.gempukku.libgdx.graph.plugin.maps.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.plugin.maps.MapsPluginPrivateData;

public class MapsRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public MapsRendererPipelineNodeProducer() {
        super(new MapsRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final String mapId = data.getString("id");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private MapsPluginPrivateData mapsPluginData;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                mapsPluginData = pipelineDataProvider.getPrivatePluginData(MapsPluginPrivateData.class);
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputs.get("camera");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();
                Camera camera = cameraInput.getValue();
                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                Map map = mapsPluginData.getMap(mapId);
                MapRenderer mapRenderer = mapsPluginData.getMapRenderer(mapId);
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
