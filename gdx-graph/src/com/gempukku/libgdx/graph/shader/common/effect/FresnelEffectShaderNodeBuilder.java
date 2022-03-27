package com.gempukku.libgdx.graph.shader.common.effect;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class FresnelEffectShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public FresnelEffectShaderNodeBuilder() {
        super(new FresnelEffectShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput viewDirValue = inputs.get("viewDir");
        FieldOutput powerValue = inputs.get("power");

        commonShaderBuilder.addMainLine("// Fresnel effect node");
        String name = "result_" + nodeId;
        String resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine("float " + name + " = pow((1.0 - clamp(dot(normalize(" + normalValue.getRepresentation() + "), normalize(" + viewDirValue.getRepresentation() + ")), 0.0, 1.0)), " + powerValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
