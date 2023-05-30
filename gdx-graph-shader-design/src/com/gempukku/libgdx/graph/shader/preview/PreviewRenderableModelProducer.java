package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;

public interface PreviewRenderableModelProducer extends PreviewModelProducer {
    void initialize(JsonValue data);

    void serialize(JsonValue value);

    Actor getCustomizationActor();
}
