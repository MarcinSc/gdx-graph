package com.gempukku.libgdx.graph.shader.common.math.common;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.common.StepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class StepShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public StepShaderNodeBuilder() {
        super(new StepShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput edgeValue = inputs.get("edge");
        if (edgeValue == null)
            edgeValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");

        ShaderFieldType resultType = inputValue.getFieldType();

        commonShaderBuilder.addMainLine("// Step node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = step(" + edgeValue.getRepresentation() + ", " + inputValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
