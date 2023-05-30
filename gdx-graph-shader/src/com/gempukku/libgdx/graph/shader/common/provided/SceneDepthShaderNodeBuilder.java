package com.gempukku.libgdx.graph.shader.common.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneDepthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SceneDepthShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SceneDepthShaderNodeBuilder() {
        super(new SceneDepthShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        graphShader.setUsingDepthTexture(true);
        if (designTime) {
            return LibGDXCollections.singletonMap("depth", new DefaultFieldOutput(ShaderFieldType.Float, "0.0"));
        } else {
            commonShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping,
                    "Near/far clipping");

            commonShaderBuilder.addUniformVariable("u_sceneDepthTexture", "sampler2D", true, UniformSetters.depthTexture,
                    "Scene depth texture");

            loadFragmentIfNotDefined(commonShaderBuilder, configuration, "unpackVec3ToFloat");

            FieldOutput screenPosition = inputs.get("screenPosition");
            String screenPositionValue = screenPosition != null ? screenPosition.getRepresentation() : "gl_FragCoord";
            String name = "depth_" + nodeId;
            commonShaderBuilder.addMainLine("// Scene depth node");
            commonShaderBuilder.addMainLine("float " + name + " = unpackVec3ToFloat(texture2D(u_sceneDepthTexture, " + screenPositionValue + ".xy).rgb, u_cameraClipping.x, u_cameraClipping.y);");
            return LibGDXCollections.singletonMap("depth", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
    }
}
