package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ShadowBlinnPhongLightingShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private final int maxNumberOfDirectionalLights;
    private final int maxNumberOfPointLights;
    private final int maxNumberOfSpotlights;
    private final float shadowAcneValue;
    private final int shadowSoftness;

    public ShadowBlinnPhongLightingShaderNodeBuilder(int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights,
                                                     float shadowAcneValue, int shadowSoftness) {
        super(new ShadowBlinnPhongLightingShaderNodeConfiguration());
        this.maxNumberOfDirectionalLights = maxNumberOfDirectionalLights;
        this.maxNumberOfPointLights = maxNumberOfPointLights;
        this.maxNumberOfSpotlights = maxNumberOfSpotlights;
        this.shadowAcneValue = shadowAcneValue;
        this.shadowSoftness = shadowSoftness;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        throw new UnsupportedOperationException("At the moment light calculation is not available in vertex shader");
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, final GraphShaderContext graphShaderContext, final GraphShader graphShader, FileHandleResolver assetResolver) {
        final String environmentId = data.getString("id", "");

        fragmentShaderBuilder.addStructure("Lighting",
                "  vec3 diffuse;\n" +
                        "  vec3 specular;\n");

//        Lighting3DUtils.configureShadowInformation(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfDirectionalLights, graphShader.getDefaultTexture());
//
//        Lighting3DUtils.configureAmbientLighting(fragmentShaderBuilder, nodeId, environmentId);


        final float[] shadowCameras = new float[16 * maxNumberOfDirectionalLights];
        fragmentShaderBuilder.addArrayUniformVariable("u_shadowCamera_" + nodeId, maxNumberOfDirectionalLights, "mat4", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            Array<Directional3DLight> directionalLights = environment.getDirectionalLights();
                            for (int i = 0; i < directionalLights.size; i++) {
                                OrthographicCamera shadowCamera = directionalLights.get(i).getShadowCamera();
                                if (shadowCamera != null) {
                                    System.arraycopy(shadowCamera.combined.val, 0, shadowCameras, i * 16, 16);
                                }
                            }
                        }
                        shader.setUniformMatrix4Array(location, shadowCameras);
                    }
                }, "Shadow camera matrix");

        final float[] shadowCamerasBufferSize = new float[1 * maxNumberOfDirectionalLights];
        fragmentShaderBuilder.addArrayUniformVariable("u_shadowCameraBufferSize_" + nodeId, maxNumberOfDirectionalLights, "float", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            Array<Directional3DLight> directionalLights = environment.getDirectionalLights();
                            for (int i = 0; i < directionalLights.size; i++) {
                                OrthographicCamera shadowCamera = directionalLights.get(i).getShadowCamera();
                                if (shadowCamera != null) {
                                    shadowCamerasBufferSize[i] = directionalLights.get(i).getShadowBufferSize();
                                }
                            }
                        }
                        shader.setUniformFloatArray(location, shadowCamerasBufferSize);
                    }
                }, "Shadow camera buffer size");

        final float[] shadowCamerasClipping = new float[2 * maxNumberOfDirectionalLights];
        fragmentShaderBuilder.addArrayUniformVariable("u_shadowCameraClipping_" + nodeId, maxNumberOfDirectionalLights, "vec2", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            Array<Directional3DLight> directionalLights = environment.getDirectionalLights();
                            for (int i = 0; i < directionalLights.size; i++) {
                                OrthographicCamera shadowCamera = directionalLights.get(i).getShadowCamera();
                                if (shadowCamera != null) {
                                    shadowCamerasClipping[i * 2 + 0] = shadowCamera.near;
                                    shadowCamerasClipping[i * 2 + 1] = shadowCamera.far;
                                }
                            }
                        }
                        shader.setUniformVector2Array(location, shadowCamerasClipping);
                    }
                }, "Shadow camera clipping");

        final float[] shadowCamerasPosition = new float[3 * maxNumberOfDirectionalLights];
        fragmentShaderBuilder.addArrayUniformVariable("u_shadowCameraPosition_" + nodeId, maxNumberOfDirectionalLights, "vec3", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            Array<Directional3DLight> directionalLights = environment.getDirectionalLights();
                            for (int i = 0; i < directionalLights.size; i++) {
                                OrthographicCamera shadowCamera = directionalLights.get(i).getShadowCamera();
                                if (shadowCamera != null) {
                                    shadowCamerasPosition[i * 3 + 0] = shadowCamera.position.x;
                                    shadowCamerasPosition[i * 3 + 1] = shadowCamera.position.y;
                                    shadowCamerasPosition[i * 3 + 2] = shadowCamera.position.z;
                                }
                            }
                        }
                        shader.setUniformVector3Array(location, shadowCamerasPosition);
                    }
                }, "Shadow camera position");

        for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
            final int lightIndex = i;
            final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>(null,
                    Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

            fragmentShaderBuilder.addUniformVariable("u_shadowMap_" + nodeId + "_" + i, "sampler2D", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null) {
                                Array<Directional3DLight> directionalLights = environment.getDirectionalLights();
                                RenderPipelineBuffer shadowFrameBuffer = null;
                                if (directionalLights.size > lightIndex)
                                    shadowFrameBuffer = directionalLights.get(lightIndex).getShadowFrameBuffer();

                                if (shadowFrameBuffer != null) {
                                    textureDescriptor.texture = shadowFrameBuffer.getColorBufferTexture();
                                } else {
                                    textureDescriptor.texture = WhitePixel.sharedInstance.texture;
                                }
                            } else {
                                textureDescriptor.texture = WhitePixel.sharedInstance.texture;
                            }
                            shader.setUniform(location, textureDescriptor);
                        }
                    }, "Shadow map " + i);
        }

        fragmentShaderBuilder.addUniformVariable("u_ambientLight_" + nodeId, "vec3", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null && environment.getAmbientColor() != null) {
                            LightColor ambientColor = environment.getAmbientColor();
                            shader.setUniform(location, ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());
                        } else {
                            shader.setUniform(location, 0f, 0f, 0f);
                        }
                    }
                }, "Ambient light");

        loadFragmentIfNotDefined(fragmentShaderBuilder, assetResolver, "unpackVec3ToFloat");

        String isLightedFunctionName = "isLighted_" + nodeId;
        String probeShadowMapFunctionName = "probeShadowMap_" + nodeId;
        fragmentShaderBuilder.addFunction(probeShadowMapFunctionName, createProbeShadowMapFunction(nodeId));

        ObjectMap<String, String> variables = new ObjectMap<>();
        variables.put("NUM_SPOT_LIGHTS", String.valueOf(maxNumberOfSpotlights));
        variables.put("NUM_POINT_LIGHTS", String.valueOf(maxNumberOfPointLights));
        variables.put("NUM_DIRECTIONAL_LIGHTS", String.valueOf(maxNumberOfDirectionalLights));
        variables.put("SHADOW_ACNE_VALUE", SimpleNumberFormatter.format(shadowAcneValue));
        variables.put("SHADOW_SOFTNESS", String.valueOf(shadowSoftness));
        variables.put("SHADOW_PROBE_COUNT", SimpleNumberFormatter.format((shadowSoftness + 1) * (shadowSoftness + 1)));
        variables.put("NODE_ID", nodeId);

        fragmentShaderBuilder.addFunction(isLightedFunctionName, GLSLFragmentReader.getFragment(assetResolver,"isLighted", variables));

        if (maxNumberOfDirectionalLights > 0)
            passDirectionalLights(environmentId, fragmentShaderBuilder, nodeId, variables, assetResolver);
        if (maxNumberOfPointLights > 0)
            passPointLights(environmentId, fragmentShaderBuilder, nodeId, variables, assetResolver);
        if (maxNumberOfSpotlights > 0)
            passSpotLights(environmentId, fragmentShaderBuilder, nodeId, variables, assetResolver);

        FieldOutput positionValue = inputs.get("position");
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput albedoValue = inputs.get("albedo");
        FieldOutput emissionValue = inputs.get("emission");
        FieldOutput specularValue = inputs.get("specular");
        FieldOutput ambientOcclusionValue = inputs.get("ambientOcclusion");
        FieldOutput shininessValue = inputs.get("shininess");

        String position = positionValue.getRepresentation();
        String normal = normalValue.getRepresentation();
        String albedo = albedoValue != null ? albedoValue.getRepresentation() : "vec3(0.0)";
        String emission = emissionValue != null ? emissionValue.getRepresentation() : "vec3(0.0)";
        String specular = specularValue != null ? specularValue.getRepresentation() : "vec3(1.0)";
        String ambientOcclusion = ambientOcclusionValue != null ? ambientOcclusionValue.getRepresentation() : "1.0";
        String shininess = shininessValue != null ? shininessValue.getRepresentation() : "32.0";

        fragmentShaderBuilder.addMainLine("// Blinn-Phong Lighting node");
        String calculateLightingFunctionName = "calculateBlinnPhongLightingFunction_" + nodeId;
        fragmentShaderBuilder.addFunction(calculateLightingFunctionName, createCalculateLightingFunction(nodeId));
        String lightingVariable = "lighting_" + nodeId;
        fragmentShaderBuilder.addMainLine("Lighting " + lightingVariable + " = " + calculateLightingFunctionName + "(" + position + ", " + normal + ", " + shininess + ");");

        ShaderFieldType resultType = ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Vector3);
        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("output")) {
            String name = "color_" + nodeId;
            fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + emission + ".rgb + " + ambientOcclusion + " * u_ambientLight_" + nodeId + " * " + albedo + ".rgb;");
            fragmentShaderBuilder.addMainLine(name + " += " + lightingVariable + ".diffuse * " + albedo + ".rgb + " + lightingVariable + ".specular * " + specular + ".rgb;");
            result.put("output", new DefaultFieldOutput(resultType.getName(), name));
        }
        if (producedOutputs.contains("diffuse")) {
            result.put("diffuse", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".diffuse"));
        }
        if (producedOutputs.contains("specularOut")) {
            result.put("specularOut", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".specular"));
        }

        return result;
    }

    private String createProbeShadowMapFunction(String nodeId) {
        StringBuilder sb = new StringBuilder();
        sb.append("vec4 probeShadowMap_" + nodeId + "(int lightIndex, vec2 coord) {\n");
        for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
            sb.append("  if (lightIndex == " + i + ")\n");
            sb.append("    return texture2D(u_shadowMap_" + nodeId + "_" + i + ", coord);\n");
        }
        sb.append("  return vec4(1.0);\n");
        sb.append("}\n");
        return sb.toString();
    }

    private String createCalculateLightingFunction(String nodeId) {
        StringBuilder sb = new StringBuilder();

        sb.append("Lighting calculateBlinnPhongLightingFunction_" + nodeId + "(vec3 position, vec3 normal, float shininess) {\n");
        sb.append("  vec3 normalizedNormal = normalize(normal);\n");
        sb.append("  Lighting lighting = Lighting(vec3(0.0), vec3(0.0));\n");
        if (maxNumberOfDirectionalLights > 0)
            sb.append("  lighting = getDirectionalBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfPointLights > 0)
            sb.append("  lighting = getPointBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfSpotlights > 0)
            sb.append("  lighting = getSpotBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        sb.append("  return lighting;\n");
        sb.append("}\n");

        return sb.toString();
    }

    private void passSpotLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configureSpotLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfSpotlights);

        fragmentShaderBuilder.addFunction("getSpotBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver,"blinn-phong/spotLightContribution", variables));
    }

    private void passPointLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configurePointLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfPointLights);

        fragmentShaderBuilder.addFunction("getPointBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver,"blinn-phong/pointLightContribution", variables));
    }

    private void passDirectionalLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configureDirectionalLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfDirectionalLights);

        fragmentShaderBuilder.addFunction("getDirectionalBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver,"blinn-phong/shadowDirectionalLightContribution", variables));
    }
}
