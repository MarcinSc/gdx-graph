package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.gempukku.libgdx.graph.pipeline.config.math.common.AbsPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class AbsPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public AbsPipelineNodeProducer() {
        super(new AbsPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return Math.abs(value);
    }
}
