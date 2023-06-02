package com.gempukku.libgdx.graph.shader.common.math.utility;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.utility.DistanceFromPlaneShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class DistanceFromPlaneShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DistanceFromPlaneShaderNodeBuilder() {
        super(new DistanceFromPlaneShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput pointValue = inputs.get("point");
        FieldOutput planePointValue = inputs.get("planePoint");
        FieldOutput planeNormalValue = inputs.get("planeNormal");

        commonShaderBuilder.addMainLine("// Distance from plane node");
        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("float " + name + " = dot(normalize(" + planeNormalValue.getRepresentation() + "), " + pointValue.getRepresentation() + " - " + planePointValue.getRepresentation() + ");");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
