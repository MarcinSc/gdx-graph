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
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class PointLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public PointLightShaderNodeBuilder() {
        super(new PointLightShaderNodeConfiguration());
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
            String name = "u_pointLightPosition_" + nodeId + "_" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Array<Point3DLight> pointLights = lightingRendererConfiguration.getPointLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
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
                            Array<Point3DLight> pointLights = lightingRendererConfiguration.getPointLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
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
                            Array<Point3DLight> pointLights = lightingRendererConfiguration.getPointLights(environmentId, shaderContext.getGraphShader(), shaderContext.getModel());
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
