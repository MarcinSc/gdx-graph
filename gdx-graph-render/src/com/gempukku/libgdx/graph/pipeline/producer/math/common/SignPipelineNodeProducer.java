package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.gempukku.libgdx.graph.pipeline.config.math.common.SignPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class SignPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public SignPipelineNodeProducer() {
        super(new SignPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return Math.signum(value);
    }
}
