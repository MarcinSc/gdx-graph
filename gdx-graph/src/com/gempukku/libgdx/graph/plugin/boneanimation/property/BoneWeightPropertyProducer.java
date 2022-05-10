package com.gempukku.libgdx.graph.plugin.boneanimation.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class BoneWeightPropertyProducer implements GraphShaderPropertyProducer {
    private final BoneWeightFieldType fieldType = new BoneWeightFieldType();

    @Override
    public ShaderFieldType getType() {
        return fieldType;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        int boneWeightCount = data.getInt("maxBoneWeightCount", 4);

        Vector2[] defaultValue = new Vector2[boneWeightCount];
        for (int i = 0; i < boneWeightCount; i++) {
            defaultValue[i] = new Vector2(0, 1);
        }

        return new PropertySource(index, name, new BoneWeightFieldType(boneWeightCount), location, defaultValue);
    }
}
