package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.DividePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class DividePipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public DividePipelineNodeProducer() {
        super(new DividePipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float a, float b) {
        return a / b;
    }
}
