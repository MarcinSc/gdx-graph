package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;

public interface PipelineRenderer extends Disposable {
    void render(RenderOutput renderOutput);
}
