package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.CosPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class CosPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public CosPipelineNodeProducer() {
        super(new CosPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.cos(value);
    }
}
