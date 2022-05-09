package com.gempukku.libgdx.graph.plugin.boneanimation.property;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
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

public class BoneTransformFieldType implements ShaderFieldType {
    public static final String type = "BoneTransforms";
    private final int maxBoneCount;

    public BoneTransformFieldType() {
        this(4);
    }

    public BoneTransformFieldType(int maxBoneCount) {
        this.maxBoneCount = maxBoneCount;
    }

    public int getMaxBoneCount() {
        return maxBoneCount;
    }

    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Matrix4[];
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
    public Object convertFromJson(JsonValue data) {
        return null;
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        int boneCount = ((BoneTransformFieldType) propertySource.getShaderFieldType()).getMaxBoneCount();
        String variableName = "u_property_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addArrayUniformVariable(variableName, boneCount, "mat4", true,
                new GlobalBonesUniformSetter(boneCount, propertySource), "Skeletal bones - " + propertySource.getPropertyName());

        return new DefaultFieldOutput(new BoneTransformFieldType(boneCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        int boneCount = ((BoneTransformFieldType) propertySource.getShaderFieldType()).getMaxBoneCount();
        String variableName = "u_property_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addArrayUniformVariable(variableName, boneCount, "mat4", false,
                new LocalBonesUniformSetter(boneCount, propertySource), "Skeletal bones");

        return new DefaultFieldOutput(new BoneTransformFieldType(boneCount), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, PropertySource propertySource) {
        throw new GdxRuntimeException("Unable to set bone transforms as attributes");
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        throw new GdxRuntimeException("Unable to set bone transforms as attributes");
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        throw new GdxRuntimeException("Unable to set bone transforms as attributes");
    }

    private static class GlobalBonesUniformSetter implements UniformRegistry.UniformSetter {
        private final Matrix4 idtMatrix = new Matrix4();
        private final PropertySource propertySource;
        private final float[] bones;

        public GlobalBonesUniformSetter(int boneCount, PropertySource propertySource) {
            this.propertySource = propertySource;
            this.bones = new float[boneCount * 16];
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Object value = shaderContext.getGlobalProperty(propertySource.getPropertyName());
            Matrix4[] valueToUse = (Matrix4[]) propertySource.getValueToUse(value);
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (valueToUse == null || idx >= valueToUse.length || valueToUse[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(valueToUse[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrix4Array(location, bones);
        }
    }

    private static class LocalBonesUniformSetter implements UniformRegistry.UniformSetter {
        private final Matrix4 idtMatrix = new Matrix4();
        private final PropertySource propertySource;
        private final float[] bones;

        public LocalBonesUniformSetter(int boneCount, PropertySource propertySource) {
            this.propertySource = propertySource;
            this.bones = new float[boneCount * 16];
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Object value = shaderContext.getLocalProperty(propertySource.getPropertyName());
            Matrix4[] valueToUse = (Matrix4[]) propertySource.getValueToUse(value);
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (valueToUse == null || idx >= valueToUse.length || valueToUse[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(valueToUse[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrix4Array(location, bones);
        }
    }

}
