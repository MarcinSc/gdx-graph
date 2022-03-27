package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.ArcsinPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class ArcsinPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ArcsinPipelineNodeProducer() {
        super(new ArcsinPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.asin(value);
    }
}
