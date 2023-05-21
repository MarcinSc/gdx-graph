package com.gempukku.libgdx.graph.shader.common.math.trigonometry;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.Arctan2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.FloatShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class Arctan2ShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public Arctan2ShaderNodeBuilder() {
        super(new Arctan2ShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType resultType = new FloatShaderFieldType();

        commonShaderBuilder.addMainLine("// Arctangent2 node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = atan(" + inputValue.getRepresentation() + ".y, " + inputValue.getRepresentation() + ".x);");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
