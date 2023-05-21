package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.ExponentialBase2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class ExponentialBase2PipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ExponentialBase2PipelineNodeProducer() {
        super(new ExponentialBase2PipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.pow(2, value);
    }
}
