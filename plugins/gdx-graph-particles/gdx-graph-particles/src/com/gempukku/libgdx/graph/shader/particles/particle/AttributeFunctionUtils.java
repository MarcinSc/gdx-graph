package com.gempukku.libgdx.graph.shader.particles.particle;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class AttributeFunctionUtils {
    public static ShaderPropertySource findAttributeWithFunction(GraphShader graphShader, String attributeFunction) {
        for (ShaderPropertySource propertySource : graphShader.getProperties().values()) {
            if (propertySource.getPropertyLocation() == PropertyLocation.Attribute) {
                if (attributeFunction.equals(propertySource.getAttributeFunction())) {
                    return propertySource;
                }
            }
        }
        return null;
    }

    public static GraphShaderNodeBuilder.FieldOutput getOutputWithFallbackVertex(
            ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> inputs,
            VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader,
            String inputName, String attributeFunction, String defaultValue) {
        GraphShaderNodeBuilder.FieldOutput birthOutput = inputs.get(inputName);
        if (birthOutput != null)
            return birthOutput;
        ShaderPropertySource particleBirthProperty = findAttributeWithFunction(graphShader, attributeFunction);
        if (particleBirthProperty != null)
            return particleBirthProperty.getShaderFieldType().addAsVertexAttribute(vertexShaderBuilder, null, particleBirthProperty);

        return new DefaultFieldOutput(ShaderFieldType.Float, defaultValue);
    }

    public static GraphShaderNodeBuilder.FieldOutput getOutputWithFallbackFragment(
            ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> inputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader,
            String inputName, String attributeFunction, String defaultValue) {
        GraphShaderNodeBuilder.FieldOutput birthOutput = inputs.get(inputName);
        if (birthOutput != null)
            return birthOutput;
        ShaderPropertySource particleBirthProperty = findAttributeWithFunction(graphShader, attributeFunction);
        if (particleBirthProperty != null)
            return particleBirthProperty.getShaderFieldType().addAsFragmentAttribute(vertexShaderBuilder, fragmentShaderBuilder, null, particleBirthProperty);

        return new DefaultFieldOutput(ShaderFieldType.Float, defaultValue);
    }

}
