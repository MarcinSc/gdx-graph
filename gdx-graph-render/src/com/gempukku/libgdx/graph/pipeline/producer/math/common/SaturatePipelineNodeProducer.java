package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SaturatePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class SaturatePipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public SaturatePipelineNodeProducer() {
        super(new SaturatePipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.clamp(value, 0, 1);
    }
}
