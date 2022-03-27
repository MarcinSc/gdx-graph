package com.gempukku.libgdx.graph.shader.common.math.arithmetic;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.DivideShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class DivideShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DivideShaderNodeBuilder() {
        super(new DivideShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        ShaderFieldType resultType = determineOutputType(aValue, bValue);

        commonShaderBuilder.addMainLine("// Divide node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + aValue.getRepresentation() + " / " + bValue.getRepresentation() + ";");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private ShaderFieldType determineOutputType(FieldOutput a, FieldOutput b) {
        ShaderFieldType aType = a.getFieldType();
        ShaderFieldType bType = b.getFieldType();
        if (aType.getName().equals(ShaderFieldType.Float))
            return bType;
        if (bType.getName().equals(ShaderFieldType.Float))
            return aType;
        if (aType != bType)
            throw new IllegalStateException("Invalid mix of input field types");
        return aType;
    }
}
