package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.BorderDetectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class BorderDetectionShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public BorderDetectionShaderNodeBuilder() {
        super(new BorderDetectionShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput textureValue = inputs.get("texture");
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput pixelSizeValue = inputs.get("pixelSize");
        FieldOutput outlineWidthValue = inputs.get("outlineWidth");
        if (outlineWidthValue == null) {
            outlineWidthValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");
        }
        FieldOutput alphaEdgeValue = inputs.get("alphaEdge");
        if (alphaEdgeValue == null) {
            alphaEdgeValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");
        }

        commonShaderBuilder.addMainLine("// Sampler2D Node");
        loadFragmentIfNotDefined(commonShaderBuilder, "borderDetection");

        String directions = "vec4("
                + (producedOutputs.contains("left") ? "1.0" : "0.0") + ", "
                + (producedOutputs.contains("right") ? "1.0" : "0.0") + ", "
                + (producedOutputs.contains("up") ? "1.0" : "0.0") + ", "
                + (producedOutputs.contains("down") ? "1.0" : "0.0")
                + ")";

        String borderName = "border_" + nodeId;

        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        commonShaderBuilder.addMainLine("vec4 " + borderName + " = borderDetection(" + textureValue.getSamplerRepresentation() + ", " + textureValue + ", " + uvValue + ", " + pixelSizeValue + ", " + directions + ", " + outlineWidthValue + ", " + alphaEdgeValue + ");");
        if (producedOutputs.contains("left")) {
            String name = "left_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + borderName + ".x;");
            result.put("left", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("right")) {
            String name = "right_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + borderName + ".y;");
            result.put("right", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("up")) {
            String name = "up_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + borderName + ".z;");
            result.put("up", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("down")) {
            String name = "down_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + borderName + ".w;");
            result.put("down", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
