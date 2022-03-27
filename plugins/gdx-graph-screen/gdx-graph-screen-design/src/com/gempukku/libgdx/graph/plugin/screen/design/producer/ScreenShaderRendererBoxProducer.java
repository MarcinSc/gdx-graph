package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ScreenShaderRendererBoxProducer extends GraphBoxProducerImpl {
    public ScreenShaderRendererBoxProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        ScreenShaderBoxPart screenShaderBoxPart = new ScreenShaderBoxPart();
        screenShaderBoxPart.initialize(data);
        result.addGraphBoxPart(screenShaderBoxPart);

        return result;
    }
}
