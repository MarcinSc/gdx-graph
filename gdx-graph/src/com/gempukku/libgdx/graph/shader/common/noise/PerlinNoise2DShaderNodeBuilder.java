package com.gempukku.libgdx.graph.shader.common.noise;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.RemapShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.config.common.noise.PerlinNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class PerlinNoise2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public PerlinNoise2DShaderNodeBuilder() {
        super(new PerlinNoise2DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput progressValue = inputs.get("progress");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput rangeValue = inputs.get("range");

        String scale = scaleValue != null ? scaleValue.getRepresentation() : "1.0";

        String name = "result_" + nodeId;
        String output;
        commonShaderBuilder.addMainLine("// Perlin noise 2D node");

        if (progressValue != null) {
            loadFragmentIfNotDefined(commonShaderBuilder, "noise/common");
            loadFragmentIfNotDefined(commonShaderBuilder, "noise/perlinNoise3d");

            if (uvValue.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
                output = "perlinNoise3d(vec3(" + uvValue.getRepresentation() + " * " + scale + ", " + progressValue.getRepresentation() + "))";
            } else {
                output = "perlinNoise3d(vec3(" + uvValue.getRepresentation() + " * " + scale + ", 0.0, " + progressValue.getRepresentation() + "))";
            }
        } else {
            loadFragmentIfNotDefined(commonShaderBuilder, "noise/common");
            loadFragmentIfNotDefined(commonShaderBuilder, "noise/perlinNoise2d");

            if (uvValue.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
                output = "perlinNoise2d(" + uvValue.getRepresentation() + " * " + scale + ")";
            } else {
                output = "perlinNoise2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scale + ")";
            }
        }

        String noiseRange = "vec2(-1.0, 1.0)";
        if (rangeValue != null) {
            String functionName = RemapShaderNodeBuilder.appendRemapFunction(commonShaderBuilder, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Float));
            commonShaderBuilder.addMainLine("float " + name + " = " + functionName + "(" + output + ", " + noiseRange + ", " + rangeValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
