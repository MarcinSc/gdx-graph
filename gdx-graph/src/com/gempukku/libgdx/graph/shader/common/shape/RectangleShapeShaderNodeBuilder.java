package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.RectangleShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class RectangleShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RectangleShapeShaderNodeBuilder() {
        super(new RectangleShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput sizeValue = inputs.get("size");
        FieldOutput borderValue = inputs.get("border");
        if (sizeValue != null && sizeValue.getFieldType().getName().equals(ShaderFieldType.Float))
            sizeValue = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(" + sizeValue.getRepresentation() + ")");

        String uv = uvValue.getRepresentation();
        String size = sizeValue != null ? sizeValue.getRepresentation() : "vec2(1.0)";
        String border = borderValue != null ? borderValue.getRepresentation() : "0.0";

        commonShaderBuilder.addMainLine("// Rectangle shape node");
        String temp1 = "temp_" + nodeId;
        String name = "result_" + nodeId;
        String resultType = ShaderFieldType.Float;

        commonShaderBuilder.addMainLine("vec2 " + temp1 + " = abs(" + uv + " * 2.0 - 1.0);");
        commonShaderBuilder.addMainLine("float " + name + " = smoothstep(0.0, " + border + ", min(" + size + ".x - " + temp1 + ".x, " + size + ".y - " + temp1 + ".y));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
