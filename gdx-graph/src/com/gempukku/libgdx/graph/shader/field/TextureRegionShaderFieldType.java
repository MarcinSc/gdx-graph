package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class TextureRegionShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.graphics.g2d.TextureRegion;
    }

    @Override
    public String getShaderType() {
        return "vec4";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "TextureRegion";
    }

    @Override
    public boolean isTexture() {
        return true;
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        final String name = propertySource.getPropertyName();

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformVariableName = "u_uvTransform_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(name);
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + name);
        commonShaderBuilder.addUniformVariable(uvTransformVariableName, "vec4", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(name);
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);
        return new DefaultFieldOutput(getName(), uvTransformVariableName, textureVariableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        final String name = propertySource.getPropertyName();

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformVariableName = "u_uvTransform_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(name);
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + name);
        commonShaderBuilder.addUniformVariable(uvTransformVariableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(name);
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);
        return new DefaultFieldOutput(getName(), uvTransformVariableName, textureVariableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, final PropertySource propertySource) {
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformAttributeName = "a_property_" + propertySource.getPropertyIndex();
        vertexShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = propertySource.getPropertyName();
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + propertySource.getPropertyName());
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), "vec4", "TextureUV property - " + propertySource.getPropertyName());

        return new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformAttributeName, textureVariableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, final PropertySource propertySource) {
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformAttributeName = "a_property_" + propertySource.getPropertyIndex();
        String uvTransformVariableName = "v_property_" + propertySource.getPropertyIndex();
        fragmentShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + propertySource.getPropertyName());
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), "vec4", "TextureUV property - " + propertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(uvTransformVariableName)) {
            vertexShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
            vertexShaderBuilder.addMainLine(uvTransformVariableName + " = " + uvTransformAttributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
        }

        return new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformVariableName, textureVariableName);
    }

    @Override
    public void setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        TextureRegion region = (TextureRegion) value;
        vertices[startIndex + 0] = region.getU();
        vertices[startIndex + 1] = region.getV();
        vertices[startIndex + 2] = region.getU2() - region.getU();
        vertices[startIndex + 3] = region.getV2() - region.getV();
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        if (data == null)
            return null;
        return data.getString("previewPath", null);
    }
}
