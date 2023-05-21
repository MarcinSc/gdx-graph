package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.InverseSquareRootPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class InverseSquareRootPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public InverseSquareRootPipelineNodeProducer() {
        super(new InverseSquareRootPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return 1f / (float) Math.sqrt(value);
    }
}
