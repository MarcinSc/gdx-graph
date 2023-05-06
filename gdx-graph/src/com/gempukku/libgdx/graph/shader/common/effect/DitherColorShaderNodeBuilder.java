package com.gempukku.libgdx.graph.shader.common.effect;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.effect.DitherColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class DitherColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DitherColorShaderNodeBuilder() {
        super(new DitherColorShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        FieldOutput colorValue = inputs.get("color");
        FieldOutput positionValue = inputs.get("position");
        FieldOutput pixelSizeValue = inputs.get("pixelSize");

        commonShaderBuilder.addMainLine("// Dither color node");
        String name = "result_" + nodeId;
        String resultType = ShaderFieldType.Vector4;

        int ditherSize = data.getInt("ditherSize", 4);

        loadFragmentIfNotDefined(commonShaderBuilder, assetResolver, "dither/dither" + ditherSize);

        String getDitherColorFunctionName = "getDitherColor_" + nodeId;
        String getDitherColorFunction = createGetDitherColorFunction(getDitherColorFunctionName, ditherSize);
        commonShaderBuilder.addFunction(getDitherColorFunctionName, getDitherColorFunction);

        commonShaderBuilder.addMainLine("vec4 " + name + " = " + getDitherColorFunctionName + "(" + positionValue.getRepresentation() + ", " + pixelSizeValue.getRepresentation() + ", " + colorValue.getRepresentation() + ");\n");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private String createGetDitherColorFunction(String functionName, int ditherSize) {
        StringBuilder sb = new StringBuilder();
        sb.append("vec4 " + functionName + "(vec2 position, vec2 pixelSize, vec4 color) {\n");
        sb.append("  vec4 colorMin = floor(color * 255.0) / 255.0;\n");
        sb.append("  vec4 colorMax = ceil(color * 255.0) / 255.0;\n");
        sb.append("  if (colorMin == colorMax)\n");
        sb.append("    return color;");
        sb.append("  vec4 colorDiff = color - colorMin;\n");
        sb.append("  vec4 colorMaxDiff = colorMax - colorMin;\n");
        sb.append("  float ditherInputValue = (colorDiff.r + colorDiff.g + colorDiff.b + colorDiff.a)\n");
        sb.append("    / (colorMaxDiff.r + colorMaxDiff.g + colorMaxDiff.b + colorMaxDiff.a);\n");
        sb.append("  return getDither" + ditherSize + "(position, pixelSize, ditherInputValue) == 0.0 ? colorMin : colorMax;\n");
        sb.append("}\n");

        return sb.toString();
    }
}
