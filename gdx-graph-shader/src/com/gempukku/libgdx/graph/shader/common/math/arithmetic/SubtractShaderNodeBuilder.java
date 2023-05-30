package com.gempukku.libgdx.graph.shader.common.math.arithmetic;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.SubtractShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SubtractShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SubtractShaderNodeBuilder() {
        super(new SubtractShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        ShaderFieldType resultType = determineOutputType(aValue, bValue);

        commonShaderBuilder.addMainLine("// Subtract node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + aValue.getRepresentation() + " - " + bValue.getRepresentation() + ";");

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
