package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.field.Matrix4ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Matrix4ShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new Matrix4ShaderFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, String attributeFunction, boolean designTime, PipelineRendererConfiguration configuration) {
        return new DefaultShaderPropertySource(index, name, type, location, attributeFunction, type.convertFromJson(data));
    }
}
