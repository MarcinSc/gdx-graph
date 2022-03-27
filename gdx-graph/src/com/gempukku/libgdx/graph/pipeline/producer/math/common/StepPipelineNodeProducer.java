package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.gempukku.libgdx.graph.pipeline.config.math.common.StepPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class StepPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public StepPipelineNodeProducer() {
        super(new StepPipelineNodeConfiguration(), "input", "edge", "output");
    }

    @Override
    protected float executeFunction(float a, float edge) {
        if (a < edge)
            return 0;
        return 1;
    }
}
