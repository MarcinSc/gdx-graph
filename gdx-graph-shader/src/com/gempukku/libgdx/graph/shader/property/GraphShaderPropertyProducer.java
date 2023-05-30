package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public interface GraphShaderPropertyProducer {
    ShaderFieldType getType();

    ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, String attributeFunction, boolean designTime, PipelineRendererConfiguration configuration);
}
