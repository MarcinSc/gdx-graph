package com.gempukku.libgdx.graph.shader.common.math.geometric;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.LengthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class LengthShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public LengthShaderNodeBuilder() {
        super(new LengthShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput inputValue = inputs.get("input");
        String resultType = ShaderFieldType.Float;

        commonShaderBuilder.addMainLine("// Length node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("float " + name + " = length(" + inputValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
