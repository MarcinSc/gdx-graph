package com.gempukku.libgdx.graph.shader.common.math.value;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.value.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class MergeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public MergeShaderNodeBuilder() {
        super(new MergeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput xValue = inputs.get("x");
        FieldOutput yValue = inputs.get("y");
        FieldOutput zValue = inputs.get("z");
        FieldOutput wValue = inputs.get("w");

        String x = xValue != null ? xValue.getRepresentation() : "0.0";
        String y = yValue != null ? yValue.getRepresentation() : "0.0";
        String z = zValue != null ? zValue.getRepresentation() : "0.0";
        String w = wValue != null ? wValue.getRepresentation() : "0.0";

        commonShaderBuilder.addMainLine("// Merge Node");

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
}
