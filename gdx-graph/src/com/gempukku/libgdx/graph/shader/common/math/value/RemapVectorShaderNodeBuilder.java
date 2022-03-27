package com.gempukku.libgdx.graph.shader.common.math.value;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapVectorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class RemapVectorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RemapVectorShaderNodeBuilder() {
        super(new RemapVectorShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");

        String x = processValue(data.getString("x"), inputValue);
        String y = processValue(data.getString("y"), inputValue);
        String z = processValue(data.getString("z"), inputValue);
        String w = processValue(data.getString("w"), inputValue);

        commonShaderBuilder.addMainLine("// Merge Vector Node");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("v2")) {
            String name = "v2_" + nodeId;
            commonShaderBuilder.addMainLine("vec2 " + name + " = vec2(" + x + ", " + y + ");");
            result.put("v2", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
        }
        if (producedOutputs.contains("v3")) {
            String name = "v3_" + nodeId;
            commonShaderBuilder.addMainLine("vec3 " + name + " = vec3(" + x + ", " + y + ", " + z + ");");
            result.put("v3", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "color_" + nodeId;
            commonShaderBuilder.addMainLine("vec4 " + name + " = vec4(" + x + ", " + y + ", " + z + ", " + w + ");");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
        }
        return result;
    }

    private String processValue(String field, FieldOutput input) {
        if (field.equals("X")) {
            if (input.getFieldType().getName().equals(ShaderFieldType.Float))
                return input.getRepresentation();
            else
                return input.getRepresentation() + ".x";
        }
        if (field.equals("Y")) {
            if (input.getFieldType().getName().equals(ShaderFieldType.Float))
                return "0.0";
            else
                return input.getRepresentation() + ".y";
        }
        if (field.equals("Z")) {
            if (input.getFieldType().getName().equals(ShaderFieldType.Float) || input.getFieldType().getName().equals(ShaderFieldType.Vector2))
                return "0.0";
            else
                return input.getRepresentation() + ".z";
        }
        if (field.equals("W")) {
            if (input.getFieldType().getName().equals(ShaderFieldType.Vector4))
                return input.getRepresentation() + ".w";
            else
                return "0.0";
        }
        return field;
    }

    public static String appendRemapFunction(CommonShaderBuilder commonShaderBuilder, ShaderFieldType resultType) {
        String functionName = "remap_" + resultType.getShaderType();

        if (!commonShaderBuilder.containsFunction(functionName)) {
            commonShaderBuilder.addFunction(functionName, resultType.getShaderType() + " " + functionName + "(" + resultType.getShaderType() + " value, vec2 from, vec2 to) {\n" +
                    "  return to.x + (value - from.x) * (to.y - to.x) / (from.y - from.x);\n" +
                    "}\n");
        }
        return functionName;
    }
}
