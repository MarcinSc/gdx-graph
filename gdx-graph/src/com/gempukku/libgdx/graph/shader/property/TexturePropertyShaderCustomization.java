package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class TexturePropertyShaderCustomization implements PropertyShaderCustomization {
    @Override
    public void processConfiguration(ShaderFieldType shaderFieldType, PropertyNodeConfiguration propertyNodeConfiguration) {
        if (shaderFieldType.isTexture()) {
            propertyNodeConfiguration.addNodeOutput(new DefaultGraphNodeOutput("textureSize", "Texture Size", ShaderFieldType.Vector2));
        }
    }

    @Override
    public void processVertexNode(ShaderFieldType fieldType, PropertyLocation propertyLocation, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> result) {
        if (fieldType.isTexture() && producedOutputs.contains("textureSize")) {
            final String name = shaderPropertySource.getPropertyName();
            if (propertyLocation == PropertyLocation.Attribute) {
                String sizeAttributeName = "a_textureSize_" + shaderPropertySource.getPropertyIndex();
                vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 2, sizeAttributeName), "vec2", "TextureSize property - " + shaderPropertySource.getPropertyName());
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeAttributeName));
            } else if (propertyLocation == PropertyLocation.Uniform) {
                String sizeVariableName = "u_textureSize_" + shaderPropertySource.getPropertyIndex();
                vertexShaderBuilder.addUniformVariable(sizeVariableName, "vec2", false,
                        new UniformRegistry.UniformSetter() {
                            @Override
                            public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                                Object value = shaderContext.getLocalProperty(name);
                                value = shaderPropertySource.getValueToUse(value);
                                if (value == null)
                                    value = new TextureRegion(shader.getDefaultTexture());
                                TextureRegion region = (TextureRegion) value;
                                shader.setUniform(location, (float) region.getRegionWidth(), (float) region.getRegionHeight());
                            }
                        }, "Texture property size - " + name);
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeVariableName));
            } else {
                String sizeVariableName = "u_textureSize_" + shaderPropertySource.getPropertyIndex();
                vertexShaderBuilder.addUniformVariable(sizeVariableName, "vec2", true,
                        new UniformRegistry.UniformSetter() {
                            @Override
                            public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                                Object value = shaderContext.getGlobalProperty(name);
                                value = shaderPropertySource.getValueToUse(value);
                                if (value == null)
                                    value = new TextureRegion(shader.getDefaultTexture());
                                TextureRegion region = (TextureRegion) value;
                                shader.setUniform(location, (float) region.getRegionWidth(), (float) region.getRegionHeight());
                            }
                        }, "Texture property size - " + name);
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeVariableName));
            }
        }
    }

    @Override
    public void processFragmentNode(ShaderFieldType fieldType, PropertyLocation propertyLocation, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, final ShaderPropertySource shaderPropertySource, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> result) {
        if (fieldType.isTexture() && producedOutputs.contains("textureSize")) {
            final String name = shaderPropertySource.getPropertyName();
            if (propertyLocation == PropertyLocation.Attribute) {
                String sizeAttributeName = "a_textureSize_" + shaderPropertySource.getPropertyIndex();
                String sizeVariableName = "v_textureSize_" + shaderPropertySource.getPropertyIndex();
                vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 2, sizeAttributeName), "vec2", "TextureSize property - " + shaderPropertySource.getPropertyName());
                if (!vertexShaderBuilder.hasVaryingVariable(sizeVariableName)) {
                    vertexShaderBuilder.addVaryingVariable(sizeVariableName, "vec2");
                    vertexShaderBuilder.addMainLine(sizeVariableName + " = " + sizeAttributeName + ";");

                    fragmentShaderBuilder.addVaryingVariable(sizeVariableName, "vec2");
                }
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeVariableName));
            } else if (propertyLocation == PropertyLocation.Uniform) {
                String sizeVariableName = "u_textureSize_" + shaderPropertySource.getPropertyIndex();
                fragmentShaderBuilder.addUniformVariable(sizeVariableName, "vec2", false,
                        new UniformRegistry.UniformSetter() {
                            @Override
                            public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                                Object value = shaderContext.getLocalProperty(name);
                                value = shaderPropertySource.getValueToUse(value);
                                if (value == null)
                                    value = new TextureRegion(shader.getDefaultTexture());
                                TextureRegion region = (TextureRegion) value;
                                shader.setUniform(location, (float) region.getRegionWidth(), (float) region.getRegionHeight());
                            }
                        }, "Texture property size - " + name);
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeVariableName));
            } else {
                String sizeVariableName = "u_textureSize_" + shaderPropertySource.getPropertyIndex();
                fragmentShaderBuilder.addUniformVariable(sizeVariableName, "vec2", true,
                        new UniformRegistry.UniformSetter() {
                            @Override
                            public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                                Object value = shaderContext.getGlobalProperty(name);
                                value = shaderPropertySource.getValueToUse(value);
                                if (value == null)
                                    value = new TextureRegion(shader.getDefaultTexture());
                                TextureRegion region = (TextureRegion) value;
                                shader.setUniform(location, (float) region.getRegionWidth(), (float) region.getRegionHeight());
                            }
                        }, "Texture property size - " + name);
                result.put("textureSize", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeVariableName));
            }
        }
    }
}
