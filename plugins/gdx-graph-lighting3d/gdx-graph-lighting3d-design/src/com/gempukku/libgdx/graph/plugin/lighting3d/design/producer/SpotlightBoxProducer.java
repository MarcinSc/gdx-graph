package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.SpotLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.IndexBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class SpotlightBoxProducer extends GraphBoxProducerImpl {
    public SpotlightBoxProducer() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);

        StringBoxPart envId = new StringBoxPart("Env id: ", "id");
        envId.initialize(data);
        result.addGraphBoxPart(envId);

        addConfigurationInputsAndOutputs(result);

        IndexBoxPart indexPart = new IndexBoxPart("Index", "index");
        indexPart.initialize(data);
        result.addGraphBoxPart(indexPart);

        return result;
    }
}