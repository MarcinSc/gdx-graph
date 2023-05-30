package com.gempukku.libgdx.graph.shader.preview;

import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;

public interface PreviewRenderableModel extends RenderableModel {
    void update(float currentTime);

    WritablePropertyContainer getPropertyContainer();
}
