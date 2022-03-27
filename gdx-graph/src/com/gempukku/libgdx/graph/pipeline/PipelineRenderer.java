package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;

public interface PipelineRenderer extends PipelinePropertySource, Disposable {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    <T> T getPluginData(Class<T> clazz);

    void render(RenderOutput renderOutput);
}
