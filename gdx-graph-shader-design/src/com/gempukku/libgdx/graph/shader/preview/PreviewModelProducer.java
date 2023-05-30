package com.gempukku.libgdx.graph.shader.preview;

import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphShader;

public interface PreviewModelProducer {
    PreviewRenderableModel create(GraphShader graphShader, PropertyContainer localPropertyContainer);
}
