package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ModelShaderRendererBoxProducer extends GraphBoxProducerImpl {
    public ModelShaderRendererBoxProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        EnumSelectBoxPart renderOrderSelect = new EnumSelectBoxPart("Render order", "renderOrder",
                new ToStringEnum<RenderOrder>(), RenderOrder.values());
        renderOrderSelect.initialize(data);
        result.addGraphBoxPart(renderOrderSelect);

        ModelShadersBoxPart graphBoxPart = new ModelShadersBoxPart();
        graphBoxPart.initialize(data);
        result.addGraphBoxPart(graphBoxPart);

        return result;
    }
}
