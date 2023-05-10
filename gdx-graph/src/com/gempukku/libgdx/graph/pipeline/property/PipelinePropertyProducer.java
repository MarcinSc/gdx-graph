package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;

public interface PipelinePropertyProducer {
    String getType();

    PipelineProperty createProperty(JsonValue data);
}
