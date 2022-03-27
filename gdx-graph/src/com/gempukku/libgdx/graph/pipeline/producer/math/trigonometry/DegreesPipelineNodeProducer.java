package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.DegreesPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;

public class DegreesPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public DegreesPipelineNodeProducer() {
        super(new DegreesPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float value) {
        return MathUtils.radiansToDegrees * value;
    }
}
