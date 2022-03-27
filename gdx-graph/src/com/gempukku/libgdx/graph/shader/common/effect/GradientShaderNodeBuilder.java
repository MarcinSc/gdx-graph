package com.gempukku.libgdx.graph.shader.common.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.effect.GradientShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;

public class GradientShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public GradientShaderNodeBuilder() {
        super(new GradientShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");

        Array<ColorPoint> pointArray = new Array<>();
        JsonValue points = data.get("points");
        for (String point : points.asStringArray()) {
            String[] split = point.split(",");
            pointArray.add(new ColorPoint(Color.valueOf(split[0]), Float.parseFloat(split[1])));
        }
        ClampMethod clampMethod = ClampMethod.valueOf(data.getString("clamp", "Normal"));

        String remapValueFunctionName = "gradient_" + nodeId;

        String functionText = createGradientFunction(remapValueFunctionName, pointArray, clampMethod);
        commonShaderBuilder.addFunction(remapValueFunctionName, functionText);

        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Gradient node");
        commonShaderBuilder.addMainLine("vec4 " + name + " = " + remapValueFunctionName + "(" + inputValue + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
    }

    private String createGradientFunction(String remapValueFunctionName, Array<ColorPoint> pointArray, ClampMethod clampMethod) {
        StringBuilder sb = new StringBuilder();

        sb.append("vec4 " + remapValueFunctionName + "(float value) {\n");
        sb.append("  value = " + clampMethod.getShaderCode("value") + ";\n");
        sb.append("  vec4 result = vec4(0.0);\n");
        float lastX = 0;
        Color lastColor = pointArray.get(0).color;
        for (ColorPoint point : pointArray) {
            if (lastX != point.pos) {
                sb.append("  result += step(" + SimpleNumberFormatter.format(lastX) + ", value) * (1.0 - step(" + SimpleNumberFormatter.format(point.pos) + ", value)) " +
                        "* mix(" +
                        "vec4(" + SimpleNumberFormatter.format(lastColor.r) + ", " + SimpleNumberFormatter.format(lastColor.g) + ", " + SimpleNumberFormatter.format(lastColor.b) + ", " + SimpleNumberFormatter.format(lastColor.a) + "), " +
                        "vec4(" + SimpleNumberFormatter.format(point.color.r) + ", " + SimpleNumberFormatter.format(point.color.g) + ", " + SimpleNumberFormatter.format(point.color.b) + ", " + SimpleNumberFormatter.format(point.color.a) + "), " +
                        "(value - " + SimpleNumberFormatter.format(lastX) + ") / (" + SimpleNumberFormatter.format(point.pos) + " - " + SimpleNumberFormatter.format(lastX) + "));\n");
            }
            lastX = point.pos;
            lastColor = point.color;
        }
        if (lastX != 1f) {
            sb.append("  result += step(" + SimpleNumberFormatter.format(lastX) + ", value) " +
                    "* vec4(" + SimpleNumberFormatter.format(lastColor.r) + ", " + SimpleNumberFormatter.format(lastColor.g) + ", " + SimpleNumberFormatter.format(lastColor.b) + ", " + SimpleNumberFormatter.format(lastColor.a) + ");\n");
        }

        sb.append("  return result;\n");
        sb.append("}\n");

        return sb.toString();
    }
}
