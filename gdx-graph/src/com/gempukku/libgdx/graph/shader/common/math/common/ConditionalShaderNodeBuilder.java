package com.gempukku.libgdx.graph.shader.common.math.common;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ConditionalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ConditionalShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ConditionalShaderNodeBuilder() {
        super(new ConditionalShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput a = inputs.get("a");
        FieldOutput b = inputs.get("b");
        FieldOutput aTrue = inputs.get("true");
        FieldOutput aFalse = inputs.get("false");
        String operation = data.getString("operation");
        String aggregate = data.getString("aggregate");

        ShaderFieldType resultType = aTrue.getFieldType();

        commonShaderBuilder.addMainLine("// Conditional node");
        String name = "result_" + nodeId;
        if (a.getFieldType().getName().equals(ShaderFieldType.Float)) {
            commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = (" + a.getRepresentation() + " " + operation + " " + b.getRepresentation() + ") ? " + aTrue.getRepresentation() + " : " + aFalse.getRepresentation() + ";");
        } else {
            String function = getFunction(operation);
            commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + aggregate + "(" + function + "(" + a.getRepresentation() + ", " + b.getRepresentation() + ")) ? " + aTrue.getRepresentation() + " : " + aFalse.getRepresentation() + ";");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private String getFunction(String operation) {
        switch (operation) {
            case ">":
                return "greaterThan";
            case ">=":
                return "greaterThanEqual";
            case "==":
                return "equal";
            case "<=":
                return "lessThanEqual";
            case "<":
                return "lessThan";
            case "!=":
                return "notEqual";
        }
        return null;
    }
}
