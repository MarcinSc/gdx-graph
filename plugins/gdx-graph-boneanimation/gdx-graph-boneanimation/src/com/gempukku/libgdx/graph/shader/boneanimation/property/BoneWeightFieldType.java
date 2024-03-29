package com.gempukku.libgdx.graph.shader.boneanimation.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ArrayShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class BoneWeightFieldType implements ArrayShaderFieldType {
    public static final String type = "BoneWeights";
    private final int maxBoneWeightCount;

    public BoneWeightFieldType() {
        this(4);
    }

    public BoneWeightFieldType(int maxBoneWeightCount) {
        this.maxBoneWeightCount = maxBoneWeightCount;
    }

    @Override
    public int getArrayLength() {
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

    private int getBoneWeightCount(ShaderPropertySource shaderPropertySource) {
        return ((BoneWeightFieldType) shaderPropertySource.getShaderFieldType()).getArrayLength();
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        int boneWeightCount = getBoneWeightCount(shaderPropertySource);
        String variableName = shaderPropertySource.getUniformName();
        for (int i = 0; i < boneWeightCount; i++) {
            final int finalI = i;
            commonShaderBuilder.addUniformVariable(variableName + "_" + i, "vec2", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Object value = shaderContext.getGlobalProperty(shaderPropertySource.getPropertyName());
                            Vector2 valueToUse = ((Vector2[]) shaderPropertySource.getValueToUse(value))[finalI];
                            shader.setUniform(location, valueToUse);
                        }
                    }, "Bone-weight property - " + shaderPropertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        int boneWeightCount = getBoneWeightCount(shaderPropertySource);
        String variableName = shaderPropertySource.getUniformName();
        for (int i = 0; i < boneWeightCount; i++) {
            final int finalI = i;
            commonShaderBuilder.addUniformVariable(variableName + "_" + i, "vec2", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Object value = shaderContext.getLocalProperty(shaderPropertySource.getPropertyName());
                            Vector2 valueToUse = ((Vector2[]) shaderPropertySource.getValueToUse(value))[finalI];
                            shader.setUniform(location, valueToUse);
                        }
                    }, "Bone-weight property - " + shaderPropertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource) {
        int boneWeightCount = getBoneWeightCount(shaderPropertySource);
        for (int i = 0; i < boneWeightCount; i++) {
            vertexShaderBuilder.addAttributeVariable(shaderPropertySource.getAttributeName(i), 2, "vec2", "Bone-weight property - " + shaderPropertySource.getPropertyName() + " - " + i);
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), shaderPropertySource.getAttributeName());
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, ShaderPropertySource shaderPropertySource) {
        int boneWeightCount = getBoneWeightCount(shaderPropertySource);
        for (int i = 0; i < boneWeightCount; i++) {
            String attributeName = shaderPropertySource.getAttributeName(i);
            String variableName = shaderPropertySource.getVariableName(i);
            vertexShaderBuilder.addAttributeVariable(attributeName, 2, "vec2", "Bone-weight property - " + shaderPropertySource.getPropertyName() + " - " + i);
            if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
                vertexShaderBuilder.addVaryingVariable(variableName, "vec2");
                vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

                fragmentShaderBuilder.addVaryingVariable(variableName + "_" + i, "vec2");
            }
        }

        return new DefaultFieldOutput(new BoneWeightFieldType(boneWeightCount), shaderPropertySource.getAttributeName());
    }

    @Override
    public void setValueInAttributesArray(String attributeName, float[] vertices, int startIndex, Object value) {
        int boneWeightIndex = Integer.parseInt(attributeName.substring(attributeName.lastIndexOf('_') + 1));
        com.badlogic.gdx.math.Vector2[] result = (Vector2[]) value;
        vertices[startIndex + 0] = result[boneWeightIndex].x;
        vertices[startIndex + 1] = result[boneWeightIndex].y;
    }
}
