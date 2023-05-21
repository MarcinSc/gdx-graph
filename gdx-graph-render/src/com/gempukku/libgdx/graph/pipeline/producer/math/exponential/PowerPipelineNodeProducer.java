package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.gempukku.libgdx.graph.pipeline.config.math.exponential.PowerPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class PowerPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public PowerPipelineNodeProducer() {
        super(new PowerPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float a, float b) {
        return (float) Math.pow(a, b);
    }
}
