package com.gempukku.libgdx.graph.ui.shader.producer.texture;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class UVFlipbookShaderBoxProducer extends GraphBoxProducerImpl {
    public UVFlipbookShaderBoxProducer() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        CheckboxBoxPart invertX = new CheckboxBoxPart("Invert X", "invertX");
        invertX.initialize(data);
        result.addGraphBoxPart(invertX);
        CheckboxBoxPart invertY = new CheckboxBoxPart("Invert Y", "invertY");
        invertY.initialize(data);
        result.addGraphBoxPart(invertY);

        return result;
    }
}
