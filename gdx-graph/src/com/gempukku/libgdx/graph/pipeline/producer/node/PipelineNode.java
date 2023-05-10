package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public interface PipelineNode extends Disposable {
    void initializePipeline();

    ObjectMap<String, FieldOutput<?>> getOutputs();

    void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs);

    void processPipelineRequirements(PipelineRequirements pipelineRequirements);

    void startFrame();

    void executeNode(PipelineRenderingContext pipelineRenderingContext,
                     PipelineRequirementsCallback pipelineRequirementsCallback);

    void endFrame();

    interface PipelineRequirementsCallback {
        PipelineRequirements getPipelineRequirements();
    }

    interface FieldOutput<T> {
        String getPropertyType();

        T getValue();
    }
}
