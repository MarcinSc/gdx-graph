package com.gempukku.libgdx.graph.shader.common.value;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ValueColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ValueColorShaderNodeBuilder() {
        super(new ValueColorShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        final Color color = Color.valueOf(data.getString("color"));

        String value = "vec4(" + format(color.r) + ", " + format(color.g) + ", " + format(color.b) + ", " + format(color.a) + ")";
        return LibGDXCollections.mapWithOne("value", new DefaultFieldOutput(ShaderFieldType.Vector4, value));
    }

    private String format(float component) {
        return SimpleNumberFormatter.format(component);
    }
}
