package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVTilingAndOffsetShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class UVTilingAndOffsetShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public UVTilingAndOffsetShaderNodeBuilder() {
        super(new UVTilingAndOffsetShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput tilingValue = inputs.get("tiling");
        FieldOutput offsetValue = inputs.get("offset");
        if (tilingValue == null)
            tilingValue = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(1.0)");
        if (offsetValue == null)
            offsetValue = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(0.0)");

        commonShaderBuilder.addMainLine("// UV Tiling & Offset Node");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("vec2 " + resultName + " = " + uvValue + " * " + tilingValue + " + " + offsetValue + ";");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector2, resultName));
    }
}
