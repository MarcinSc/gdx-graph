package com.gempukku.libgdx.graph.plugin.particles.model;

import com.gempukku.libgdx.graph.plugin.sprites.ValuePerVertex;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class ParticlesUtil {
    public static void setParticleAttribute(float[] particlesData, int particleOffset,
                                            int vertexCount, int vertexLength, int attributeOffset,
                                            PropertySource propertySource, Object attributeValue) {
        ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
        if (attributeValue instanceof ValuePerVertex) {
            for (int i = 0; i < vertexCount; i++) {
                int vertexIndex = particleOffset + i * vertexLength;
                Object vertexValue = ((ValuePerVertex) attributeValue).getValue(i);
                shaderFieldType.setValueInAttributesArray(particlesData, vertexIndex + attributeOffset, propertySource.getValueToUse(vertexValue));
            }
        } else {
            Object value = propertySource.getValueToUse(attributeValue);
            for (int i = 0; i < vertexCount; i++) {
                int vertexIndex = particleOffset + i * vertexLength;
                shaderFieldType.setValueInAttributesArray(particlesData, vertexIndex + attributeOffset, value);
            }
        }
    }
}
