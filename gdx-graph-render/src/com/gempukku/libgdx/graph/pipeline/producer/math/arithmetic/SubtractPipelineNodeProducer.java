package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.SubtractPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class SubtractPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public SubtractPipelineNodeProducer() {
        super(new SubtractPipelineNodeConfiguration(), "inputA", "inputB", "output");
    }

    @Override
    protected float executeFunction(float a, float b) {
        return a - b;
    }
}
