package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Producer;

public interface PreviewRenderableModelProducer extends Producer<PreviewRenderableModel> {
    void initialize(JsonValue data);

    void serialize(JsonValue value);

    Actor getCustomizationActor();
}
