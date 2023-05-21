package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.config.math.common.ModuloPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.TwoParamMathFunctionPipelineNodeProducer;

public class ModuloPipelineNodeProducer extends TwoParamMathFunctionPipelineNodeProducer {
    public ModuloPipelineNodeProducer() {
        super(new ModuloPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(float input1, float input2) {
        return input1 - input2 * MathUtils.floor(input1 / input2);
    }
}
