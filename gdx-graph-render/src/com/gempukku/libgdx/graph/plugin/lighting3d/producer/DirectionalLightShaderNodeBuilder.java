package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.LightColor;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.provider.Lights3DProvider;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class DirectionalLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DirectionalLightShaderNodeBuilder() {
        super(new DirectionalLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        final int index = data.getInt("index");
        final String environmentId = data.getString("id", "");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("direction")) {
            String name = "u_directionalLightDirection_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Directional3DLight> directionalLights = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), index + 1);
                            if (directionalLights != null && directionalLights.size > index && directionalLights.get(index) != null) {
                                Directional3DLight directionalLight = directionalLights.get(index);
                                shader.setUniform(location, directionalLight.getDirectionX(), directionalLight.getDirectionY(), directionalLight.getDirectionZ());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    }, "Light direction");
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_directionalLightColor_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Directional3DLight> directionalLights = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), index + 1);
                            if (directionalLights != null && directionalLights.size > index && directionalLights.get(index) != null) {
                                Directional3DLight directionalLight = directionalLights.get(index);
                                LightColor color = directionalLight.getColor();
                                shader.setUniform(location, color.getRed(), color.getGreen(), color.getBlue(), 1f);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    }, "Light color");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
        }
        return result;
    }
}
