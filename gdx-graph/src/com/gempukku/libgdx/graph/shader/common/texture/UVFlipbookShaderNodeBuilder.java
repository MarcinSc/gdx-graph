package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class UVFlipbookShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public UVFlipbookShaderNodeBuilder() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput tileCountValue = inputs.get("tileCount");
        FieldOutput indexValue = inputs.get("index");
        FieldOutput loopingValue = inputs.get("looping");
        if (loopingValue == null) {
            loopingValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");
        }

        loadFragmentIfNotDefined(commonShaderBuilder, "uvFlipbook");

        commonShaderBuilder.addMainLine("// UV Flipbook Node");
        boolean invertX = data.getBoolean("invertX");
        boolean invertY = data.getBoolean("invertY");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("vec2 " + resultName + " = uvFlipbook(" + uvValue + ", int(" + tileCountValue + ".x), int(" + tileCountValue + ".y), int(" + indexValue + "), " + loopingValue + " == 1.0, bvec2(" + invertX + ", " + invertY + "));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector2, resultName));
    }
}
