package com.gempukku.libgdx.graph.plugin;

import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public interface RuntimePipelinePlugin {
    void update(TimeProvider timeProvider);
}
