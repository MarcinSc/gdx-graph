package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.plugin.lighting3d.LightColor;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.provider.Lights3DProvider;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class AmbientLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public AmbientLightShaderNodeBuilder() {
        super(new AmbientLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String environmentId = data.getString("id", "");

        commonShaderBuilder.addUniformVariable("u_ambientLight_" + nodeId, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DPrivateData privatePluginData = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class);
                        Lighting3DEnvironment environment = privatePluginData.getEnvironment(environmentId);
                        Lights3DProvider lights3DProvider = privatePluginData.getLights3DProvider();
                        LightColor ambientColor = lights3DProvider.getAmbientLight(environment, shaderContext.getRenderableModel());
                        if (ambientColor != null) {
                            shader.setUniform(location, ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue(), 1f);
                        } else {
                            shader.setUniform(location, 0f, 0f, 0f, 1f);
                        }
                    }
                }, "Ambient light");
        return LibGDXCollections.singletonMap("ambient", new DefaultFieldOutput(ShaderFieldType.Vector4, "u_ambientLight_" + nodeId));
    }
}
