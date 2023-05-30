package com.gempukku.libgdx.graph.shader.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelsUniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.ObjectNormalToWorldShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ObjectNormalToWorldShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ObjectNormalToWorldShaderNodeBuilder() {
        super(new ObjectNormalToWorldShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        commonShaderBuilder.addUniformVariable("u_normalWorldTrans", "mat4", false, ModelsUniformSetters.normalWorldTrans,
                "Model normal to world transformation");
        FieldOutput input = inputs.get("input");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Object Normal to World Node");
        commonShaderBuilder.addMainLine("vec3 " + resultName + " = normalize((u_normalWorldTrans * vec4(" + input.getRepresentation() + ", 0.0)).xyz);");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, resultName));
    }
}
