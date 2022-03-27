package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.ClampPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.ThreeParamMathFunctionPipelineNodeProducer;

public class ClampPipelineNodeProducer extends ThreeParamMathFunctionPipelineNodeProducer {
    public ClampPipelineNodeProducer() {
        super(new ClampPipelineNodeConfiguration(), "input", "min", "max", "output");
    }

    @Override
    protected float executeFunction(float a, float b, float c) {
        return MathUtils.clamp(a, b, c);
    }
}
