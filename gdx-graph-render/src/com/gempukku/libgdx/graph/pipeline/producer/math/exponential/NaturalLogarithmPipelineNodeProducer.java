package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.NaturalLogarithmPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class NaturalLogarithmPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public NaturalLogarithmPipelineNodeProducer() {
        super(new NaturalLogarithmPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.log(value);
    }
}
