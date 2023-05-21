package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.LogarithmBase2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class LogarithmBase2PipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public LogarithmBase2PipelineNodeProducer() {
        super(new LogarithmBase2PipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.log2(value);
    }
}
