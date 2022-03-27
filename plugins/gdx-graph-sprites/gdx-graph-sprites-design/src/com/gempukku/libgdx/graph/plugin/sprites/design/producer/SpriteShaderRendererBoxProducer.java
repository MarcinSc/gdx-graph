package com.gempukku.libgdx.graph.plugin.sprites.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpriteShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class SpriteShaderRendererBoxProducer extends GraphBoxProducerImpl {
    public SpriteShaderRendererBoxProducer() {
        super(new SpriteShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        EnumSelectBoxPart renderOrderSelect = new EnumSelectBoxPart("Render order", "renderOrder",
                new ToStringEnum<RenderOrder>(), RenderOrder.values());
        renderOrderSelect.initialize(data);
        result.addGraphBoxPart(renderOrderSelect);

        SpriteShadersBoxPart graphBoxPart = new SpriteShadersBoxPart();
        graphBoxPart.initialize(data);
        result.addGraphBoxPart(graphBoxPart);
        return result;
    }
}
