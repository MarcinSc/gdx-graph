package com.gempukku.libgdx.graph.shader.common.math.exponential;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.ExponentialBase2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ExponentialBase2ShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ExponentialBase2ShaderNodeBuilder() {
        super(new ExponentialBase2ShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType resultType = inputValue.getFieldType();

        commonShaderBuilder.addMainLine("// Exponential base 2 node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = exp2(" + inputValue.getRepresentation() + ");");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
