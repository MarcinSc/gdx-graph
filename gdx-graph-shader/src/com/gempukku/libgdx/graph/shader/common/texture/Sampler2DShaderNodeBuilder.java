package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class Sampler2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public Sampler2DShaderNodeBuilder() {
        super(new Sampler2DShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        TextureFieldOutput textureValue = (TextureFieldOutput) inputs.get("texture");
        FieldOutput uvValue = inputs.get("uv");

        loadFragmentIfNotDefined(commonShaderBuilder, configuration, "textureWrap");

        commonShaderBuilder.addMainLine("// Sampler2D Node");
        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        String colorName = "color_" + nodeId;
        String uCalculatedName = "uCalculated_" + nodeId;
        String vCalculatedName = "vCalculated_" + nodeId;

        commonShaderBuilder.addMainLine("float " + uCalculatedName + " = " + getUVCalculation(textureValue.getUWrap(), uvValue.getRepresentation() + ".x") + ";");
        commonShaderBuilder.addMainLine("float " + vCalculatedName + " = " + getUVCalculation(textureValue.getVWrap(), uvValue.getRepresentation() + ".y") + ";");
        commonShaderBuilder.addMainLine("vec4 " + colorName + " = texture2D(" + textureValue.getSamplerRepresentation() + ", " + textureValue.getRepresentation() + ".xy + vec2(" + uCalculatedName + ", " + vCalculatedName + ") * " + textureValue.getRepresentation() + ".zw);");

        result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, colorName));
        if (producedOutputs.contains("r")) {
            String name = "r_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".r;");
            result.put("r", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("g")) {
            String name = "g_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".g;");
            result.put("g", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("b")) {
            String name = "b_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".b;");
            result.put("b", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("a")) {
            String name = "a_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".a;");
            result.put("a", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }

    private String getUVCalculation(Texture.TextureWrap textureWrap, String valueRepresentation) {
        switch (textureWrap) {
            case ClampToEdge: {
                return "clampToEdge(" + valueRepresentation + ")";
            }
            case Repeat: {
                return "repeat(" + valueRepresentation + ")";
            }
            case MirroredRepeat: {
                return "mirroredRepeat(" + valueRepresentation + ")";
            }
        }
        return null;
    }
}
