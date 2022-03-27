package com.gempukku.libgdx.graph.plugin.callback.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.callback.producer.RenderCallbackPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class RenderCallbackBoxProducer extends GraphBoxProducerImpl {
    public RenderCallbackBoxProducer() {
        super(new RenderCallbackPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);

        StringBoxPart callbackId = new StringBoxPart("Callback id: ", "callbackId");
        callbackId.initialize(data);
        result.addGraphBoxPart(callbackId);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}