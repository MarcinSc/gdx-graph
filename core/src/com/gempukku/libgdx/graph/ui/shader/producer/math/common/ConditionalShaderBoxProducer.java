package com.gempukku.libgdx.graph.ui.shader.producer.math.common;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ConditionalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ConditionalShaderBoxProducer extends GraphBoxProducerImpl {
    public ConditionalShaderBoxProducer() {
        super(new ConditionalShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        SelectBoxPart operationType = new SelectBoxPart("Operation", "operation",
                ">", ">=", "==", "<=", "<", "!=");
        operationType.initialize(data);
        result.addGraphBoxPart(operationType);
        SelectBoxPart aggregationType = new SelectBoxPart("Aggregate", "aggregate",
                "any", "all");
        aggregationType.initialize(data);
        result.addGraphBoxPart(aggregationType);

        return result;
    }
}
