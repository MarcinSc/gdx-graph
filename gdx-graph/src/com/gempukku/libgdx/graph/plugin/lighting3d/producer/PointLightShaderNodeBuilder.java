package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.lighting3d.LightColor;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.provider.Lights3DProvider;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderContext;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class PointLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public PointLightShaderNodeBuilder() {
        super(new PointLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final int index = data.getInt("index");
        final String environmentId = data.getString("id", "");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("position")) {
            String name = "u_pointLightPosition_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Point3DLight> pointLights = lights3DProvider.getPointLights(environment, ((ModelShaderContext) shaderContext).getRenderableModel(), index + 1);
                            if (pointLights != null && pointLights.size > index && pointLights.get(index) != null) {
                                Point3DLight pointLight = pointLights.get(index);
                                shader.setUniform(location, pointLight.getPosition());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    }, "Point light position");
            result.put("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_pointLightColor_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Point3DLight> pointLights = lights3DProvider.getPointLights(environment, ((ModelShaderContext) shaderContext).getRenderableModel(), index + 1);
                            if (pointLights != null && pointLights.size > index && pointLights.get(index) != null) {
                                Point3DLight pointLight = pointLights.get(index);
                                LightColor color = pointLight.getColor();
                                shader.setUniform(location, color.getRed(), color.getGreen(), color.getBlue(), 1f);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    }, "Point light color");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
        }
        if (producedOutputs.contains("intensity")) {
            String name = "u_pointLightIntensity_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "float", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Point3DLight> pointLights = lights3DProvider.getPointLights(environment, ((ModelShaderContext) shaderContext).getRenderableModel(), index + 1);
                            if (pointLights != null && pointLights.size > index && pointLights.get(index) != null) {
                                Point3DLight pointLight = pointLights.get(index);
                                shader.setUniform(location, pointLight.getIntensity());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    }, "Point light intensity");
            result.put("intensity", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
