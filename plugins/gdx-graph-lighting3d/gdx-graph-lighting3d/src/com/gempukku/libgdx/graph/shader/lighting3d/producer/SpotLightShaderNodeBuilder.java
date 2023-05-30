package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Spot3DLight;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SpotLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SpotLightShaderNodeBuilder() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        final LightingRendererConfiguration lightingRendererConfiguration = configuration.getConfig(LightingRendererConfiguration.class);
        if (lightingRendererConfiguration == null)
            throw new GdxRuntimeException("A configuration with class LightingRendererConfiguration needs to be define for pipeline");

        final int index = data.getInt("index");
        final String environmentId = data.getString("id", "");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("position")) {
            String name = "u_spotLightPosition_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getPosition());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    }, "Spot light position");
            result.put("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("direction")) {
            String name = "u_spotLightDirection_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getDirectionX(), spotLight.getDirectionY(), spotLight.getDirectionZ());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    }, "Spot light direction");
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_spotLightColor_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                LightColor color = spotLight.getColor();
                                shader.setUniform(location, color.getRed(), color.getGreen(), color.getBlue(), 1f);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    }, "Spot light color");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
        }
        if (producedOutputs.contains("intensity")) {
            String name = "u_spotLightIntensity_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "float", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getIntensity());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    }, "Spot light intensity");
            result.put("intensity", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("cutOffAngle")) {
            String name = "u_spotLightCutOffAngle_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "float", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getCutoffAngle());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    }, "Spot light cut-off angle");
            result.put("cutOffAngle", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("exponent")) {
            String name = "u_spotLightExponent_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "float", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Spot3DLight> spotLights = lightingRendererConfiguration.getSpotLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
                            if (spotLights != null && spotLights.size > index && spotLights.get(index) != null) {
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getExponent());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    }, "Spot light exponent");
            result.put("exponent", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
