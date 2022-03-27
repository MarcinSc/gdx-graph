package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.LerpPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.ThreeParamMathFunctionPipelineNodeProducer;

public class LerpPipelineNodeProducer extends ThreeParamMathFunctionPipelineNodeProducer {
    public LerpPipelineNodeProducer() {
        super(new LerpPipelineNodeConfiguration(), "a", "b", "t", "output");
    }

    @Override
    protected float executeFunction(float a, float b, float c) {
        return MathUtils.lerp(a, b, c);
    }
}
