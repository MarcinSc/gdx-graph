package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.TextureSettingsGraphBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class SceneColorShaderBoxProducer extends GraphBoxProducerImpl {
    public SceneColorShaderBoxProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        TextureSettingsGraphBoxPart textureSettingsPart = new TextureSettingsGraphBoxPart();
        textureSettingsPart.initialize(data);
        result.addGraphBoxPart(textureSettingsPart);

        return result;
    }
}
