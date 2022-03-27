package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.provided.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.IntegerBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.util.Validators;

public class SkinningShaderBoxProducer extends GraphBoxProducerImpl {
    public SkinningShaderBoxProducer() {
        super(new SkinningShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);

        IntegerBoxPart boneCount = new IntegerBoxPart("Bone count", "boneCount", 12, new Validators.GreaterThanValidator(0));
        IntegerBoxPart boneWeightCount = new IntegerBoxPart("Weight count", "boneWeightCount", 5, new Validators.GreaterThanValidator(0));
        boneCount.initialize(data);
        boneWeightCount.initialize(data);
        result.addGraphBoxPart(boneCount);
        result.addGraphBoxPart(boneWeightCount);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
