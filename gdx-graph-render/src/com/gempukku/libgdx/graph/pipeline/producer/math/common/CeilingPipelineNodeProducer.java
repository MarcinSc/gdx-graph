package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.CeilingPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class CeilingPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public CeilingPipelineNodeProducer() {
        super(new CeilingPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.ceil(value);
    }
}
