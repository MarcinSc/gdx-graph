package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.gempukku.libgdx.graph.pipeline.config.math.common.MaximumPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class MaximumPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public MaximumPipelineNodeProducer() {
        super(new MaximumPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float input1, float input2) {
        return Math.max(input1, input2);
    }
}
