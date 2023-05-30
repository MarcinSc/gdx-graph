package com.gempukku.libgdx.graph.shader.common.effect;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.effect.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class IntensityShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public IntensityShaderNodeBuilder() {
        super(new IntensityShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput aValue = inputs.get("color");

        commonShaderBuilder.addMainLine("// Intensity node");
        String name = "result_" + nodeId;
        String resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine("float " + name + " = dot(" + aValue.getRepresentation() + ".rgb, vec3(0.2126729, 0.7151522, 0.0721750));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
