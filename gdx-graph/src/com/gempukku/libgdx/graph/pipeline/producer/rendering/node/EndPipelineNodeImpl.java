package com.gempukku.libgdx.graph.pipeline.producer.rendering.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.AbstractPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.EndPipelineNode;

public class EndPipelineNodeImpl extends AbstractPipelineNode implements EndPipelineNode {
    private RenderPipeline renderPipeline;
    private FieldOutput<RenderPipeline> inputField;

    public EndPipelineNodeImpl() {
        super(null);
    }

    @Override
    public void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs) {
        inputField = (FieldOutput<RenderPipeline>) inputs.get("input").get(0);
    }

    @Override
    public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
        renderPipeline = inputField.getValue();
    }

    @Override
    public RenderPipeline getRenderPipeline() {
        return renderPipeline;
    }
}
