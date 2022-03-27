package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.TanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class TanPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public TanPipelineNodeProducer() {
        super(new TanPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.tan(value);
    }
}
