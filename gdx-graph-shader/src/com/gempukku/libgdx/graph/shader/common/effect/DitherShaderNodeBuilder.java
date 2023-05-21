package com.gempukku.libgdx.graph.shader.common.effect;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.effect.DitherShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class DitherShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DitherShaderNodeBuilder() {
        super(new DitherShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput positionValue = inputs.get("position");
        FieldOutput pixelSizeValue = inputs.get("pixelSize");

        commonShaderBuilder.addMainLine("// Dither node");
        String name = "result_" + nodeId;
        String resultType = ShaderFieldType.Float;

        int ditherSize = data.getInt("ditherSize", 4);

        loadFragmentIfNotDefined(commonShaderBuilder, assetResolver, "dither/dither" + ditherSize);

        commonShaderBuilder.addMainLine("float " + name + " = getDither" + ditherSize + "(" + positionValue.getRepresentation() + ", " + pixelSizeValue.getRepresentation() + ", " + inputValue.getRepresentation() + ");\n");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
