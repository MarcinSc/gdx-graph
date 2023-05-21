package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.ArctanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class ArctanPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ArctanPipelineNodeProducer() {
        super(new ArctanPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return (float) Math.atan(value);
    }
}
