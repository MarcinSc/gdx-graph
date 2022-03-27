package com.gempukku.libgdx.graph.pipeline.producer.rendering.node;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.RenderPipelineImpl;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;

public class StartPipelineNode extends SingleInputsPipelineNode {
    private final DefaultFieldOutput<RenderPipeline> output;
    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(ObjectMap<String, FieldOutput<?>> outputs) {
        super(outputs);
        output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        outputs.put("output", output);
    }

    @Override
    public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
        renderPipeline = new RenderPipelineImpl(pipelineDataProvider.getBufferCopyHelper(),
                pipelineDataProvider.getTextureBufferCache());

        output.setValue(renderPipeline);
    }

    @Override
    public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
        FieldOutput<Vector2> sizeInput = (FieldOutput<Vector2>) inputs.get("size");
        FieldOutput<Color> backgroundInput = (FieldOutput<Color>) inputs.get("background");
        float bufferX = (sizeInput != null) ? sizeInput.getValue().x : pipelineRenderingContext.getRenderWidth();
        float bufferY = (sizeInput != null) ? sizeInput.getValue().y : pipelineRenderingContext.getRenderHeight();

        Color backgroundColor = (backgroundInput != null) ? backgroundInput.getValue() : Color.BLACK;

        int width = MathUtils.round(bufferX);
        int height = MathUtils.round(bufferY);

        RenderPipelineBuffer frameBuffer = renderPipeline.initializeDefaultBuffer(width, height, Pixmap.Format.RGB888, backgroundColor);
        // Dummy call to make sure frame buffer is drawn
        frameBuffer.beginColor();
        frameBuffer.endColor();
    }
}
