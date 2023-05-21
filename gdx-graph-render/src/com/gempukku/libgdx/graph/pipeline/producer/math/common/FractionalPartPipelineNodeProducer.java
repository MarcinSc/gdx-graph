package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.FloorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class FractionalPartPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public FractionalPartPipelineNodeProducer() {
        super(new FloorPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return value - MathUtils.floor(value);
    }
}
