package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class UVFlipbookShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public UVFlipbookShaderNodeBuilder() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput tileCountValue = inputs.get("tileCount");
        FieldOutput indexValue = inputs.get("index");
        FieldOutput loopingValue = inputs.get("looping");
        if (loopingValue == null) {
            loopingValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");
        }

        loadFragmentIfNotDefined(commonShaderBuilder, configuration, "uvFlipbook");

        commonShaderBuilder.addMainLine("// UV Flipbook Node");
        boolean invertX = data.getBoolean("invertX");
        boolean invertY = data.getBoolean("invertY");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("vec2 " + resultName + " = uvFlipbook(" + uvValue + ", int(" + tileCountValue + ".x), int(" + tileCountValue + ".y), int(" + indexValue + "), " + loopingValue + " == 1.0, bvec2(" + invertX + ", " + invertY + "));");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Vector2, resultName));
    }
}
