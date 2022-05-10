package com.gempukku.libgdx.graph.plugin.boneanimation.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class BoneWeightFieldType implements ShaderFieldType {
    public static final String type = "BoneWeights";
    private final int maxBoneWeightCount;

    public BoneWeightFieldType() {
        this(4);
    }

    public BoneWeightFieldType(int maxBoneWeightCount) {
        this.maxBoneWeightCount = maxBoneWeightCount;
    }

    public int getMaxBoneWeightCount() {
        return maxBoneWeightCount;
    }

    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Vector2[];
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return type;
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public String getShaderType() {
        throw new UnsupportedOperationException("Unable to put bone weights into shader code directly");
    }

    @Override
    public int getNumberOfComponents() {
        return 2 * maxBoneWeightCount;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return null;
    }

    private int getBoneWeightCount(PropertySource propertySource) {
        return ((BoneWeightFieldType) propertySource.getShaderFieldType()).getMaxBoneWeightCount();
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        int boneWeightCount = getBoneWeightCount(propertySource);
        String variableName = propertySource.getUniformName();
        for (int i = 0; i < boneWeightCount; i++) {
            final int finalI = i;
            commonShaderBuilder.addUniformVariable(variableName + "_" + i, "vec2", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Object value = shaderContext.getGlobalProperty(propertySource.getPropertyName());
                            Vector2 valueToUse = ((Vector2[]) propertySource.getValueToUse(value))[finalI];
                            shader.setUniform(location, valueToUse);
                        }
                    }, "Bone-weight property - " + propertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        int boneWeightCount = getBoneWeightCount(propertySource);
        String variableName = propertySource.getUniformName();
        for (int i = 0; i < boneWeightCount; i++) {
            final int finalI = i;
            commonShaderBuilder.addUniformVariable(variableName + "_" + i, "vec2", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Object value = shaderContext.getLocalProperty(propertySource.getPropertyName());
                            Vector2 valueToUse = ((Vector2[]) propertySource.getValueToUse(value))[finalI];
                            shader.setUniform(location, valueToUse);
                        }
                    }, "Bone-weight property - " + propertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, PropertySource propertySource) {
        int boneWeightCount = getBoneWeightCount(propertySource);
        String attributeName = propertySource.getAttributeName();
        for (int i = 0; i < boneWeightCount; i++) {
            vertexShaderBuilder.addAttributeVariable(attributeName + "_" + i, 2, "vec2", "Bone-weight property - " + propertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        int boneWeightCount = getBoneWeightCount(propertySource);
        String attributeName = propertySource.getAttributeName();
        String variableName = propertySource.getVariableName();
        for (int i = 0; i < boneWeightCount; i++) {
            vertexShaderBuilder.addAttributeVariable(attributeName + "_" + i, 2, "vec2", "Bone-weight property - " + propertySource.getPropertyName() + " - " + i);
            if (!vertexShaderBuilder.hasVaryingVariable(variableName + "_" + i)) {
                vertexShaderBuilder.addVaryingVariable(variableName + "_" + i, "vec2");
                vertexShaderBuilder.addMainLine(variableName + "_" + i + " = " + attributeName + "_" + i + ";");

                fragmentShaderBuilder.addVaryingVariable(variableName + "_" + i, "vec2");
            }
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), variableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        com.badlogic.gdx.math.Vector2[] result = (Vector2[]) value;
        for (int i = 0; i < maxBoneWeightCount; i++) {
            vertices[startIndex + i * 2 + 0] = result[i].x;
            vertices[startIndex + i * 2 + 1] = result[i].y;
        }
    }
}
