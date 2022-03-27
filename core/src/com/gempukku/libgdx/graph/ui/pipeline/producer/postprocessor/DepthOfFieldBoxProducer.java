package com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.DepthOfFieldPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.util.Validators;

public class DepthOfFieldBoxProducer extends GraphBoxProducerImpl {
    public DepthOfFieldBoxProducer() {
        super(new DepthOfFieldPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);

        FloatBoxPart maxBlurPart = new FloatBoxPart("Max blur", "maxBlur", 10, new Validators.GreaterThanValidator(0, false));
        maxBlurPart.setValue(10f);
        maxBlurPart.initialize(data);
        result.addGraphBoxPart(maxBlurPart);

        CheckboxBoxPart blurBackground = new CheckboxBoxPart("Blur background", "blurBackground");
        blurBackground.setValue(false);
        blurBackground.initialize(data);
        result.addGraphBoxPart(blurBackground);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
