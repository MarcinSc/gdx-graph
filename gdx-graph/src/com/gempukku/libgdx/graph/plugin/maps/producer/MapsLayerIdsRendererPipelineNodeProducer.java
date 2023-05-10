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

public class MapsLayerIdsRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public MapsLayerIdsRendererPipelineNodeProducer() {
        super(new MapsLayerIdsRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(final JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final String mapId = data.getString("id");
        final String[] layerIds = data.getString("layers").split(",");
        final int[] ids = new int[layerIds.length];
        for (int i = 0; i < layerIds.length; i++) {
            ids[i] = Integer.parseInt(layerIds[i]);
        }

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private MapsPluginPrivateData mapsPluginData;

            @Override
            public void initializePipeline() {
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
                    mapRenderer.render(ids);
                    currentBuffer.endColor();

                    pipelineRenderingContext.getRenderContext().begin();
                }
                output.setValue(renderPipeline);
            }
        };
    }
}
