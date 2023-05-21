package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.ReciprocalPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class ReciprocalPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ReciprocalPipelineNodeProducer() {
        super(new ReciprocalPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return 1 / value;
    }
}
