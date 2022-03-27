package com.gempukku.libgdx.graph.ui.shader.producer.effect;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.effect.DitherColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class DitherColorShaderBoxProducer extends GraphBoxProducerImpl {
    public DitherColorShaderBoxProducer() {
        super(new DitherColorShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final GraphBoxImpl result = createGraphBox(id);

        SelectBoxPart ditherSizeSelect = new SelectBoxPart("Dither size:", "ditherSize", "2", "4", "8");
        ditherSizeSelect.initialize(data);

        result.addGraphBoxPart(ditherSizeSelect);

        addConfigurationInputsAndOutputs(result);

        return result;
    }
}
