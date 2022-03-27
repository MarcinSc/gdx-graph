package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.SinPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class SinPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public SinPipelineNodeProducer() {
        super(new SinPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.sin(value);
    }
}
