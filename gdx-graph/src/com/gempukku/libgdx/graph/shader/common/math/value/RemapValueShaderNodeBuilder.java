package com.gempukku.libgdx.graph.shader.common.math.value;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapValueShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;

public class RemapValueShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RemapValueShaderNodeBuilder() {
        super(new RemapValueShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");

        Array<Vector2> pointArray = new Array<>();
        JsonValue points = data.get("points");
        for (String point : points.asStringArray()) {
            String[] split = point.split(",");
            pointArray.add(new Vector2(Float.parseFloat(split[0]), Float.parseFloat(split[1])));
        }
        ClampMethod clampMethod = ClampMethod.valueOf(data.getString("clamp", "Normal"));

        String remapValueFunctionName = "remapValue_" + nodeId;

        String functionText = createRemapValueFunction(remapValueFunctionName, pointArray, clampMethod);
        commonShaderBuilder.addFunction(remapValueFunctionName, functionText);

        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Remap Value node");
        commonShaderBuilder.addMainLine("float " + name + " = " + remapValueFunctionName + "(" + inputValue + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    private String createRemapValueFunction(String remapValueFunctionName, Array<Vector2> pointArray, ClampMethod clampMethod) {
        StringBuilder sb = new StringBuilder();

        sb.append("float " + remapValueFunctionName + "(float value) {\n");
        sb.append("  value = " + clampMethod.getShaderCode("value") + ";\n");
        sb.append("  float result = 0.0;\n");
        float lastX = 0;
        float lastY = pointArray.get(0).y;
        for (Vector2 point : pointArray) {
            if (lastX != point.x) {
                sb.append("  result += step(" + SimpleNumberFormatter.format(lastX) + ", value) * (1.0 - step(" + SimpleNumberFormatter.format(point.x) + ", value)) " +
                        "* mix(" + SimpleNumberFormatter.format(lastY) + ", " + SimpleNumberFormatter.format(point.y) + ", (value - " + SimpleNumberFormatter.format(lastX) + ") / (" + SimpleNumberFormatter.format(point.x) + " - " + SimpleNumberFormatter.format(lastX) + "));\n");
            }
            lastX = point.x;
            lastY = point.y;
        }
        if (lastX != 1f) {
            sb.append("  result += step(" + SimpleNumberFormatter.format(lastX) + ", value) * " + SimpleNumberFormatter.format(lastY) + ";\n");
        }

        sb.append("  return result;\n");
        sb.append("}\n");

        return sb.toString();
    }
}
