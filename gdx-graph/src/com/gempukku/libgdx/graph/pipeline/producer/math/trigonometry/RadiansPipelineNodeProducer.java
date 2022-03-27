package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.RadiansPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class RadiansPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public RadiansPipelineNodeProducer() {
        super(new RadiansPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.degreesToRadians * value;
    }
}
