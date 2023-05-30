package com.gempukku.libgdx.graph.pipeline.producer.provided;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;

public class RenderSizePipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public RenderSizePipelineNodeProducer() {
        super(new RenderSizePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final Vector2 size = new Vector2();

        final DefaultFieldOutput<Vector2> sizeOutput = new DefaultFieldOutput<>(PipelineFieldType.Vector2);
        sizeOutput.setValue(size);
        final DefaultFieldOutput<Float> widthOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);
        final DefaultFieldOutput<Float> heightOutput = new DefaultFieldOutput<>(PipelineFieldType.Float);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        result.put("size", sizeOutput);
        result.put("width", widthOutput);
        result.put("height", heightOutput);

        return new SingleInputsPipelineNode(result, configuration) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                int width = pipelineRenderingContext.getRenderWidth();
                int height = pipelineRenderingContext.getRenderHeight();

                size.set(width, height);
                widthOutput.setValue(width * 1f);
                heightOutput.setValue(height * 1f);
            }
        };
    }
}
