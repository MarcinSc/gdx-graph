package com.gempukku.libgdx.graph.shader.common.math.common;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.common.LerpShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class LerpShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public LerpShaderNodeBuilder() {
        super(new LerpShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        FieldOutput tValue = inputs.get("t");
        ShaderFieldType resultType = aValue.getFieldType();

        commonShaderBuilder.addMainLine("// Mix (lerp) node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = mix(" + aValue.getRepresentation() + ", " + bValue.getRepresentation() + ", " + tValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
