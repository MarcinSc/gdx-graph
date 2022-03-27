package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SmoothstepPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.ThreeParamMathFunctionPipelineNodeProducer;

public class SmoothstepPipelineNodeProducer extends ThreeParamMathFunctionPipelineNodeProducer {
    public SmoothstepPipelineNodeProducer() {
        super(new SmoothstepPipelineNodeConfiguration(), "input", "edge0", "edge1", "output");
    }

    @Override
    protected float executeFunction(float x, float edge0, float edge1) {
        // Scale, bias and saturate x to 0..1 range
        x = MathUtils.clamp((x - edge0) / (edge1 - edge0), 0f, 1f);
        // Evaluate polynomial
        return x * x * (3 - 2 * x);
    }
}
