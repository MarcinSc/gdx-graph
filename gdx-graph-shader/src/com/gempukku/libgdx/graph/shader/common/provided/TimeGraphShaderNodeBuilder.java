package com.gempukku.libgdx.graph.shader.common.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class TimeGraphShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public TimeGraphShaderNodeBuilder() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        float multiplier = data.getFloat("multiplier", 1f);

        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("time")) {
            commonShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                    "Time");
            result.put("time", new DefaultFieldOutput(ShaderFieldType.Float, "(u_time * " + SimpleNumberFormatter.format(multiplier) + ")"));
        }
        if (producedOutputs.contains("sinTime")) {
            String name = "u_sinTime_" + nodeId;
            commonShaderBuilder.addUniformVariable(name, "float", true, new UniformSetters.SinTime(multiplier),
                    "sin(Time)");
            result.put("sinTime", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("cosTime")) {
            String name = "u_cosTime_" + nodeId;
            commonShaderBuilder.addUniformVariable(name, "float", true, new UniformSetters.CosTime(multiplier),
                    "cos(Time)");
            result.put("cosTime", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("deltaTime")) {
            String name = "u_deltaTime_" + nodeId;
            commonShaderBuilder.addUniformVariable(name, "float", true, new UniformSetters.DeltaTime(multiplier),
                    "Delta time");
            result.put("deltaTime", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
