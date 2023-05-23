package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.common.Producer;

public interface PreviewRenderableModelProducer extends Producer<PreviewRenderableModel> {
    Actor getCustomizationActor();
}
