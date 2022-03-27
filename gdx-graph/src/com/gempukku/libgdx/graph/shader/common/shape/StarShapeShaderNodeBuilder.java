package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.StarShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class StarShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public StarShapeShaderNodeBuilder() {
        super(new StarShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput armsValue = inputs.get("arms");
        FieldOutput minDepthValue = inputs.get("minDepth");
        FieldOutput maxDepthValue = inputs.get("maxDepth");
        FieldOutput curveValue = inputs.get("curve");

        loadFragmentIfNotDefined(commonShaderBuilder, "shape/starShape");
        String uv = uvValue.getRepresentation();
        String arms = armsValue.getRepresentation();
        String minDepth = minDepthValue != null ? minDepthValue.getRepresentation() : "0.5";
        String maxDepth = maxDepthValue != null ? maxDepthValue.getRepresentation() : "1.0";
        String curve = curveValue != null ? curveValue.getRepresentation() : "2.0";

        commonShaderBuilder.addMainLine("// Star shape node");
        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("float " + name + " = 1.0 - step(getStarValue(" + uv + ", " + arms + ", " + minDepth + ", " + maxDepth + ", " + curve + "), distance(2.0 * " + uv + ", vec2(1.0)));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
