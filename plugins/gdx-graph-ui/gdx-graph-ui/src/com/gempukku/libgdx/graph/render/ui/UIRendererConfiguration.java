package com.gempukku.libgdx.graph.render.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;

public interface UIRendererConfiguration extends RendererConfiguration {
    Stage getStage(String id);
}
