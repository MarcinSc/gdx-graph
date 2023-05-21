package com.gempukku.libgdx.graph.shader.common.noise;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiBorder3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class VoronoiBorder3DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public VoronoiBorder3DShaderNodeBuilder() {
        super(new VoronoiBorder3DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        FieldOutput pointValue = inputs.get("point");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput progressValue = inputs.get("progress");

        String scale = (scaleValue != null) ? scaleValue.getRepresentation() : "1.0";
        String progress = (progressValue != null) ? progressValue.getRepresentation() : "0.0";

        loadFragmentIfNotDefined(commonShaderBuilder, assetResolver, "voronoiBorder3d");

        commonShaderBuilder.addMainLine("// Voronoi border 3D node");
        String name = "result_" + nodeId;
        String output = "voronoiBorder3d(" + pointValue.getRepresentation() + " * " + scale + ", " + progress + ")";

        commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}