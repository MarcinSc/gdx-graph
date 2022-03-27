package com.gempukku.libgdx.graph.shader.common.math.geometric;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.DistanceShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class DistanceShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DistanceShaderNodeBuilder() {
        super(new DistanceShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput p0Value = inputs.get("p0");
        FieldOutput p1Value = inputs.get("p1");
        String resultType = ShaderFieldType.Float;

        commonShaderBuilder.addMainLine("// Distance node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("float " + name + " = distance(" + p0Value.getRepresentation() + ", " + p1Value.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
