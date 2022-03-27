package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.DotShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class DotShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DotShapeShaderNodeBuilder() {
        super(new DotShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");

        String uv = uvValue.getRepresentation();

        commonShaderBuilder.addMainLine("// Dot shape node");
        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("float " + name + " = 2.0 * clamp(0.5 - distance(" + uv + ", vec2(0.5)), 0.0, 0.5);");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
