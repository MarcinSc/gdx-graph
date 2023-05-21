package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.ExponentialPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class ExponentialPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ExponentialPipelineNodeProducer() {
        super(new ExponentialPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.exp(value);
    }
}
