package com.gempukku.libgdx.graph.shader.common.value;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueVector3ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;

public class ValueVector3ShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ValueVector3ShaderNodeBuilder() {
        super(new ValueVector3ShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        float v1 = data.getFloat("v1");
        float v2 = data.getFloat("v2");
        float v3 = data.getFloat("v3");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, "vec3(" + format(v1) + ", " + format(v2) + ", " + format(v3) + ")"));
    }

    private String format(float component) {
        return SimpleNumberFormatter.format(component);
    }
}
