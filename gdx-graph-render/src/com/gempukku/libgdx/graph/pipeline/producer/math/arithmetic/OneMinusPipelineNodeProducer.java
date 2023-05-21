package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.OneMinusPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class OneMinusPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public OneMinusPipelineNodeProducer() {
        super(new OneMinusPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return 1 - value;
    }
}
