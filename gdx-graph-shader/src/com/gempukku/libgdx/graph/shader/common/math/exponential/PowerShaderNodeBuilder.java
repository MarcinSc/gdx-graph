package com.gempukku.libgdx.graph.shader.common.math.exponential;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.PowerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class PowerShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public PowerShaderNodeBuilder() {
        super(new PowerShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        ShaderFieldType resultType = aValue.getFieldType();

        commonShaderBuilder.addMainLine("// Power node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = pow(" + aValue.getRepresentation() + ", " + bValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
