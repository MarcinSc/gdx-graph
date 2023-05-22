package com.gempukku.libgdx.graph.shader.boneanimation.property;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertySource;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class BoneTransformPropertyProducer implements GraphShaderPropertyProducer {
    private final BoneTransformFieldType fieldType = new BoneTransformFieldType();

    @Override
    public ShaderFieldType getType() {
        return fieldType;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, String attributeFunction, boolean designTime) {
        int boneCount = data.getInt("maxBoneCount", 12);

        Matrix4[] defaultValue = new Matrix4[boneCount];
        for (int i = 0; i < boneCount; i++) {
            defaultValue[i] = new Matrix4().idt();
        }

        return new DefaultShaderPropertySource(index, name, new BoneTransformFieldType(boneCount), location, attributeFunction, defaultValue);
    }
}
