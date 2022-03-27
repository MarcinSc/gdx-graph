package com.gempukku.libgdx.graph.shader.common.math.common;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.common.SmoothstepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class SmoothstepShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SmoothstepShaderNodeBuilder() {
        super(new SmoothstepShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput edge0Value = inputs.get("edge0");
        FieldOutput edge1Value = inputs.get("edge1");
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType resultType = inputValue.getFieldType();

        commonShaderBuilder.addMainLine("// Smoothstep node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = smoothstep(" + edge0Value.getRepresentation() + ", " + edge1Value.getRepresentation() + ", " + inputValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
