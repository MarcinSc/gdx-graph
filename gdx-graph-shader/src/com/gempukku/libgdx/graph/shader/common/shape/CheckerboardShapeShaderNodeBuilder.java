package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.CheckerboardShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class CheckerboardShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public CheckerboardShapeShaderNodeBuilder() {
        super(new CheckerboardShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput repeatValue = inputs.get("repeat");

        String uv = uvValue.getRepresentation();
        String repeat = repeatValue != null ? repeatValue.getRepresentation() : "vec2(1.0)";

        commonShaderBuilder.addMainLine("// Checkerboard shape node");
        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("float " + name + " = mod(dot(vec2(1.0), step(vec2(0.5), fract(" + uv + " * " + repeat + "))), 2.0);");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
