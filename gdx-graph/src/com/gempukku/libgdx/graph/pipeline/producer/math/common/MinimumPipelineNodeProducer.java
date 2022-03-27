package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.gempukku.libgdx.graph.pipeline.config.math.common.MinimumPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class MinimumPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public MinimumPipelineNodeProducer() {
        super(new MinimumPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float input1, float input2) {
        return Math.min(input1, input2);
    }
}
