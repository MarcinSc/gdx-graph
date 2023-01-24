package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class SceneColorShaderBoxProducer extends GraphBoxProducerImpl {
    public SceneColorShaderBoxProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        EnumSelectBoxPart<Texture.TextureWrap> uWrapBox = new EnumSelectBoxPart<>("U Wrap ", "uWrap",
                new StringifyEnum<Texture.TextureWrap>(), Texture.TextureWrap.values());
        EnumSelectBoxPart<Texture.TextureWrap> vWrapBox = new EnumSelectBoxPart<>("V Wrap ", "vWrap",
                new StringifyEnum<Texture.TextureWrap>(), Texture.TextureWrap.values());
        result.addGraphBoxPart(uWrapBox);
        result.addGraphBoxPart(vWrapBox);

        uWrapBox.initialize(data);
        vWrapBox.initialize(data);

        return result;
    }
}
