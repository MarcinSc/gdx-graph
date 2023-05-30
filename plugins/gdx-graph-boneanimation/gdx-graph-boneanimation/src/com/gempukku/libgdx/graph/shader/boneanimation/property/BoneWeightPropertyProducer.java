package com.gempukku.libgdx.graph.shader.boneanimation.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertySource;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class BoneWeightPropertyProducer implements GraphShaderPropertyProducer {
    private final BoneWeightFieldType fieldType = new BoneWeightFieldType();

    @Override
    public ShaderFieldType getType() {
        return fieldType;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, String attributeFunction, boolean designTime, PipelineRendererConfiguration configuration) {
        int boneWeightCount = data.getInt("maxBoneWeightCount", 4);

        Vector2[] defaultValue = new Vector2[boneWeightCount];
        for (int i = 0; i < boneWeightCount; i++) {
            defaultValue[i] = new Vector2(0, 1);
        }

        return new DefaultShaderPropertySource(index, name, new BoneWeightFieldType(boneWeightCount), location, attributeFunction, defaultValue);
    }
}
