package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.plugin.lighting3d.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.provider.Lights3DProvider;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;

public class Lighting3DUtils {
    public static void configureAmbientLighting(CommonShaderBuilder commonShaderBuilder, String nodeId, final String environmentId) {
        commonShaderBuilder.addUniformVariable("u_ambientLight_" + nodeId, "vec3", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        LightColor ambientColor = lights3DProvider.getAmbientLight(environment, shaderContext.getRenderableModel());
                        if (ambientColor != null) {
                            shader.setUniform(location, ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());
                        } else {
                            shader.setUniform(location, 0f, 0f, 0f);
                        }
                    }
                }, "Ambient light");
    }

    public static void configureSpotLighting(CommonShaderBuilder commonShaderBuilder, String nodeId, final String environmentId, final int maxNumberOfSpotlights) {
        commonShaderBuilder.addStructure("SpotLight",
                "  vec3 color;\n" +
                        "  vec3 position;\n" +
                        "  vec3 direction;\n" +
                        "  float cutoffAngle;\n" +
                        "  float exponent;\n");
        commonShaderBuilder.addStructArrayUniformVariable("u_spotLights_" + nodeId, new String[]{"color", "position", "direction", "cutoffAngle", "exponent"}, maxNumberOfSpotlights, "SpotLight", false,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Spot3DLight> spots = lights3DProvider.getSpotLights(environment, shaderContext.getRenderableModel(), maxNumberOfSpotlights);

                        for (int i = 0; i < maxNumberOfSpotlights; i++) {
                            int location = startingLocation + i * structSize;
                            if (spots != null && i < spots.size) {
                                Spot3DLight spotLight = spots.get(i);
                                LightColor color = spotLight.getColor();
                                float intensity = spotLight.getIntensity();
                                Vector3 position = spotLight.getPosition();

                                shader.setUniform(location, color.getRed() * intensity,
                                        color.getGreen() * intensity, color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], position.x, position.y, position.z);
                                shader.setUniform(location + fieldOffsets[2], spotLight.getDirectionX(), spotLight.getDirectionY(),
                                        spotLight.getDirectionZ());
                                shader.setUniform(location + fieldOffsets[3], spotLight.getCutoffAngle());
                                shader.setUniform(location + fieldOffsets[4], spotLight.getExponent());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[2], 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[3], 0f);
                                shader.setUniform(location + fieldOffsets[4], 0f);
                            }
                        }
                    }
                }, "Spot lights");
    }

    public static void configurePointLighting(CommonShaderBuilder commonShaderBuilder, String nodeId, final String environmentId, final int maxNumberOfPointLights) {
        commonShaderBuilder.addStructure("PointLight",
                "  vec3 color;\n" +
                        "  vec3 position;\n");
        commonShaderBuilder.addStructArrayUniformVariable("u_pointLights_" + nodeId, new String[]{"color", "position"}, maxNumberOfPointLights, "PointLight", false,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Point3DLight> points = lights3DProvider.getPointLights(environment, shaderContext.getRenderableModel(), maxNumberOfPointLights);

                        for (int i = 0; i < maxNumberOfPointLights; i++) {
                            int location = startingLocation + i * structSize;
                            if (points != null && i < points.size) {
                                Point3DLight pointLight = points.get(i);
                                LightColor color = pointLight.getColor();
                                float intensity = pointLight.getIntensity();
                                Vector3 position = pointLight.getPosition();

                                shader.setUniform(location, color.getRed() * intensity,
                                        color.getGreen() * intensity, color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], position.x, position.y, position.z);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                            }
                        }
                    }
                }, "Point lights");
    }

    public static void configureDirectionalLighting(CommonShaderBuilder commonShaderBuilder, String nodeId, final String environmentId, final int maxNumberOfDirectionalLights) {
        commonShaderBuilder.addStructure("DirectionalLight",
                "  vec3 color;\n" +
                        "  vec3 direction;\n");
        commonShaderBuilder.addStructArrayUniformVariable("u_dirLights_" + nodeId, new String[]{"color", "direction"}, maxNumberOfDirectionalLights, "DirectionalLight", false,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);

                        for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                            int location = startingLocation + i * structSize;
                            if (dirs != null && i < dirs.size) {
                                Directional3DLight directionalLight = dirs.get(i);
                                LightColor color = directionalLight.getColor();
                                float intensity = directionalLight.getIntensity();

                                shader.setUniform(location, color.getRed() * intensity, color.getGreen() * intensity,
                                        color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], directionalLight.getDirectionX(),
                                        directionalLight.getDirectionY(), directionalLight.getDirectionZ());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                            }
                        }
                    }
                }, "Directional lights");
    }

    public static void configureShadowInformation(CommonShaderBuilder commonShaderBuilder, String nodeId, final String environmentId, final int maxNumberOfDirectionalLights, final TextureRegion whiteTexture) {
        final float[] shadowCameras = new float[16 * maxNumberOfDirectionalLights];
        commonShaderBuilder.addArrayUniformVariable("u_shadowCamera_" + nodeId, maxNumberOfDirectionalLights, "mat4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);
                        if (dirs != null) {
                            for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                                if (dirs.size > i) {
                                    OrthographicCamera shadowCamera = dirs.get(i).getShadowCamera();
                                    if (shadowCamera != null) {
                                        System.arraycopy(shadowCamera.combined.val, 0, shadowCameras, i * 16, 16);
                                    }
                                }
                            }
                        }
                        shader.setUniformMatrix4Array(location, shadowCameras);
                    }
                }, "Shadow camera matrix");

        final float[] shadowCamerasBufferSize = new float[1 * maxNumberOfDirectionalLights];
        commonShaderBuilder.addArrayUniformVariable("u_shadowCameraBufferSize_" + nodeId, maxNumberOfDirectionalLights, "float", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);
                        if (dirs != null) {
                            for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                                if (dirs.size > i) {
                                    Directional3DLight directionalLight = dirs.get(i);
                                    OrthographicCamera shadowCamera = directionalLight.getShadowCamera();
                                    if (shadowCamera != null) {
                                        shadowCamerasBufferSize[i] = directionalLight.getShadowBufferSize();
                                    }
                                }
                            }
                        }
                        shader.setUniformFloatArray(location, shadowCamerasBufferSize);
                    }
                }, "Shadow camera buffer size");

        final float[] shadowCamerasClipping = new float[2 * maxNumberOfDirectionalLights];
        commonShaderBuilder.addArrayUniformVariable("u_shadowCameraClipping_" + nodeId, maxNumberOfDirectionalLights, "vec2", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);
                        if (dirs != null) {
                            for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                                if (dirs.size > i) {
                                    OrthographicCamera shadowCamera = dirs.get(i).getShadowCamera();
                                    if (shadowCamera != null) {
                                        shadowCamerasClipping[i * 2 + 0] = shadowCamera.near;
                                        shadowCamerasClipping[i * 2 + 1] = shadowCamera.far;
                                    }
                                }
                            }
                        }
                        shader.setUniformVector2Array(location, shadowCamerasClipping);
                    }
                }, "Shadow camera clipping");

        final float[] shadowCamerasPosition = new float[3 * maxNumberOfDirectionalLights];
        commonShaderBuilder.addArrayUniformVariable("u_shadowCameraPosition_" + nodeId, maxNumberOfDirectionalLights, "vec3", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);
                        if (dirs != null) {
                            for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                                if (dirs.size > i) {
                                    OrthographicCamera shadowCamera = dirs.get(i).getShadowCamera();
                                    if (shadowCamera != null) {
                                        shadowCamerasPosition[i * 3 + 0] = shadowCamera.position.x;
                                        shadowCamerasPosition[i * 3 + 1] = shadowCamera.position.y;
                                        shadowCamerasPosition[i * 3 + 2] = shadowCamera.position.z;
                                    }
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

            commonShaderBuilder.addUniformVariable("u_shadowMap_" + nodeId + "_" + i, "sampler2D", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                            Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                            Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                            Array<Directional3DLight> dirs = lights3DProvider.getDirectionalLights(environment, shaderContext.getRenderableModel(), maxNumberOfDirectionalLights);
                            if (dirs != null) {
                                RenderPipelineBuffer shadowFrameBuffer = null;
                                if (dirs.size > lightIndex)
                                    shadowFrameBuffer = dirs.get(lightIndex).getShadowFrameBuffer();

                                if (shadowFrameBuffer != null) {
                                    textureDescriptor.texture = shadowFrameBuffer.getColorBufferTexture();
                                } else {
                                    textureDescriptor.texture = whiteTexture.getTexture();
                                }
                            } else {
                                textureDescriptor.texture = whiteTexture.getTexture();
                            }
                            shader.setUniform(location, textureDescriptor);
                        }
                    }, "Shadow map " + i);
        }
    }
}
