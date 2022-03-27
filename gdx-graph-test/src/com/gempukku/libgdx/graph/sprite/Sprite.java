package com.gempukku.libgdx.graph.sprite;

import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface Sprite extends RenderableSprite {
    boolean cleanup(TimeProvider timeProvider, PipelineRenderer pipelineRenderer);

    void setOutline(float outline);
}
