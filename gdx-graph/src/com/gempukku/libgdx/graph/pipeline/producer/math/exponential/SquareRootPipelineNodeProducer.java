package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.SquareRootPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class SquareRootPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public SquareRootPipelineNodeProducer() {
        super(new SquareRootPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.sqrt(value);
    }
}
