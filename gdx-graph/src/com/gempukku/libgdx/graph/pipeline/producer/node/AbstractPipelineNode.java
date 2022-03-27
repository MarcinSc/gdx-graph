package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.ObjectMap;

public abstract class AbstractPipelineNode implements PipelineNode {
    private ObjectMap<String, FieldOutput<?>> outputs;

    public AbstractPipelineNode(ObjectMap<String, FieldOutput<?>> outputs) {
        this.outputs = outputs;
    }

    @Override
    public void initializePipeline(PipelineDataProvider pipelineDataProvider) {

    }

    @Override
    public ObjectMap<String, FieldOutput<?>> getOutputs() {
        return outputs;
    }

    @Override
    public void processPipelineRequirements(PipelineRequirements pipelineRequirements) {

    }

    @Override
    public void startFrame() {

    }

    @Override
    public void endFrame() {

    }

    @Override
    public void dispose() {

    }
}
