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
import com.gempukku.libgdx.graph.shader.node.DefaultTextureFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.shader.property.TextureShaderPropertySource;

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
    public int getNumberOfComponents() {
        return 4;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return ShaderFieldType.TextureRegion;
    }

    @Override
    public boolean isTexture() {
        return true;
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsGlobalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        TextureShaderPropertySource textureShaderPropertySource = (TextureShaderPropertySource) shaderPropertySource;
        final String name = shaderPropertySource.getPropertyName();

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        textureDescriptor.minFilter = textureShaderPropertySource.getMinFilter();
        textureDescriptor.magFilter = textureShaderPropertySource.getMagFilter();
        textureDescriptor.uWrap = Texture.TextureWrap.ClampToEdge;
        textureDescriptor.vWrap = Texture.TextureWrap.ClampToEdge;

        Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
        if (data.has("uWrap"))
            uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = shaderPropertySource.getUniformName();
        String uvTransformVariableName = "u_uvTransform_" + shaderPropertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getGlobalProperty(name);
                        value = shaderPropertySource.getValueToUse(value);
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
                        value = shaderPropertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);
        return new DefaultTextureFieldOutput(getName(), uvTransformVariableName, textureVariableName, uWrap, vWrap);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsLocalUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        TextureShaderPropertySource textureShaderPropertySource = (TextureShaderPropertySource) shaderPropertySource;
        final String name = shaderPropertySource.getPropertyName();

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        textureDescriptor.minFilter = textureShaderPropertySource.getMinFilter();
        textureDescriptor.magFilter = textureShaderPropertySource.getMagFilter();
        textureDescriptor.uWrap = Texture.TextureWrap.ClampToEdge;
        textureDescriptor.vWrap = Texture.TextureWrap.ClampToEdge;

        Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
        if (data.has("uWrap"))
            uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = shaderPropertySource.getUniformName();
        String uvTransformVariableName = "u_uvTransform_" + shaderPropertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(name);
                        value = shaderPropertySource.getValueToUse(value);
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
                        value = shaderPropertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);
        return new DefaultTextureFieldOutput(getName(), uvTransformVariableName, textureVariableName, uWrap, vWrap);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        TextureShaderPropertySource textureShaderPropertySource = (TextureShaderPropertySource) shaderPropertySource;
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        textureDescriptor.minFilter = textureShaderPropertySource.getMinFilter();
        textureDescriptor.magFilter = textureShaderPropertySource.getMagFilter();
        textureDescriptor.uWrap = Texture.TextureWrap.ClampToEdge;
        textureDescriptor.vWrap = Texture.TextureWrap.ClampToEdge;

        Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
        if (data.has("uWrap"))
            uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = shaderPropertySource.getUniformName();
        String uvTransformAttributeName = shaderPropertySource.getAttributeName();
        vertexShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderPropertySource.getPropertyName();
                        value = shaderPropertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + shaderPropertySource.getPropertyName());
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), "vec4", "TextureUV property - " + shaderPropertySource.getPropertyName());

        return new DefaultTextureFieldOutput(ShaderFieldType.TextureRegion, uvTransformAttributeName, textureVariableName, uWrap, vWrap);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource) {
        TextureShaderPropertySource textureShaderPropertySource = (TextureShaderPropertySource) shaderPropertySource;
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        textureDescriptor.minFilter = textureShaderPropertySource.getMinFilter();
        textureDescriptor.magFilter = textureShaderPropertySource.getMagFilter();
        textureDescriptor.uWrap = Texture.TextureWrap.ClampToEdge;
        textureDescriptor.vWrap = Texture.TextureWrap.ClampToEdge;

        Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
        if (data.has("uWrap"))
            uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = shaderPropertySource.getUniformName();
        String uvTransformAttributeName = shaderPropertySource.getAttributeName();
        String uvTransformVariableName = shaderPropertySource.getVariableName();
        fragmentShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getLocalProperty(shaderPropertySource.getPropertyName());
                        value = shaderPropertySource.getValueToUse(value);
                        if (value == null)
                            value = new TextureRegion(shader.getDefaultTexture());
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + shaderPropertySource.getPropertyName());
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), "vec4", "TextureUV property - " + shaderPropertySource.getPropertyName());
        if (!vertexShaderBuilder.hasVaryingVariable(uvTransformVariableName)) {
            vertexShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
            vertexShaderBuilder.addMainLine(uvTransformVariableName + " = " + uvTransformAttributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
        }

        return new DefaultTextureFieldOutput(ShaderFieldType.TextureRegion, uvTransformVariableName, textureVariableName, uWrap, vWrap);
    }

    @Override
    public void setValueInAttributesArray(String attributeName, float[] vertices, int startIndex, Object value) {
        TextureRegion region = (TextureRegion) value;
        if (attributeName.startsWith("a_textureSize")) {
            vertices[startIndex + 0] = region.getRegionWidth();
            vertices[startIndex + 1] = region.getRegionHeight();

        } else {
            vertices[startIndex + 0] = region.getU();
            vertices[startIndex + 1] = region.getV();
            vertices[startIndex + 2] = region.getU2() - region.getU();
            vertices[startIndex + 3] = region.getV2() - region.getV();
        }
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        if (data == null)
            return null;
        return data.getString("previewPath", null);
    }
}
