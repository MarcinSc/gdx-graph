package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.lighting3d.LightColor;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class AmbientLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public AmbientLightShaderNodeBuilder() {
        super(new AmbientLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(
            boolean designTime, String nodeId, final JsonValue data,
            ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            CommonShaderBuilder commonShaderBuilder, final GraphShader graphShader, final PipelineRendererConfiguration configuration) {
        final String environmentId = data.getString("id", "");

        commonShaderBuilder.addUniformVariable("u_ambientLight_" + nodeId, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        LightingRendererConfiguration lightingRendererConfiguration = configuration.getConfig(LightingRendererConfiguration.class);
                        LightColor ambientColor = lightingRendererConfiguration.getAmbientLight(environmentId, graphShader, shaderContext.getModel());
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
