package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ApplyNormalMapShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ApplyNormalMapShaderNodeBuilder() {
        super(new ApplyNormalMapShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        commonShaderBuilder.addMainLine("// Apply normal map");
        loadFragmentIfNotDefined(commonShaderBuilder, configuration, "applyNormalMap");

        FieldOutput normal = inputs.get("normal");
        FieldOutput tangent = inputs.get("tangent");
        FieldOutput normalMap = inputs.get("normalMap");
        FieldOutput strength = inputs.get("strength");
        String strengthValue = strength != null ? strength.getRepresentation() : "1.0";

        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("vec3 " + name + " = applyNormalMap(" + tangent.getRepresentation() + ", " + normal.getRepresentation() + ", " + normalMap.getRepresentation() + ".xyz, " + strengthValue + ");");
        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
    }
}
