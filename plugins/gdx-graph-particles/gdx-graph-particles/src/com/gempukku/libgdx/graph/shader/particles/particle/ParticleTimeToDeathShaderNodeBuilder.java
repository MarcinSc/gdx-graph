package com.gempukku.libgdx.graph.shader.particles.particle;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.particles.ParticleAttributeFunctions;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleTimeToDeathShaderNodeConfiguration;

public class ParticleTimeToDeathShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ParticleTimeToDeathShaderNodeBuilder() {
        super(new ParticleTimeToDeathShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput deathOutput = AttributeFunctionUtils.getOutputWithFallbackVertex(inputs, vertexShaderBuilder,
                graphShader, "death", ParticleAttributeFunctions.ParticleDeath, "0.0");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");

        String name = "result_" + nodeId;
        vertexShaderBuilder.addMainLine("// Particle Time To Death Node");
        vertexShaderBuilder.addMainLine("float" + " " + name + " = " + deathOutput.getRepresentation() + " - u_time;");

        return LibGDXCollections.mapWithOne("time", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput deathOutput = AttributeFunctionUtils.getOutputWithFallbackFragment(inputs, vertexShaderBuilder, fragmentShaderBuilder,
                graphShader, "death", ParticleAttributeFunctions.ParticleDeath, "0.0");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");

        String varyingName = "v_" + nodeId + "_lifetime";
        vertexShaderBuilder.addMainLine("// Particle Time To Death Node");
        vertexShaderBuilder.addVaryingVariable(varyingName, "float");
        fragmentShaderBuilder.addVaryingVariable(varyingName, "float");
        vertexShaderBuilder.addMainLine(varyingName + " = " + deathOutput.getRepresentation() + " - u_time;");

        return LibGDXCollections.mapWithOne("time", new DefaultFieldOutput(ShaderFieldType.Float, varyingName));
    }
}
