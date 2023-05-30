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
import com.gempukku.libgdx.graph.shader.particles.config.ParticleLifePercentageShaderNodeConfiguration;

public class ParticleLifePercentageShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ParticleLifePercentageShaderNodeBuilder() {
        super(new ParticleLifePercentageShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput birthOutput = AttributeFunctionUtils.getOutputWithFallbackVertex(inputs, vertexShaderBuilder,
                graphShader, "birth", ParticleAttributeFunctions.ParticleBirth, "0.0");
        FieldOutput deathOutput = AttributeFunctionUtils.getOutputWithFallbackVertex(inputs, vertexShaderBuilder,
                graphShader, "death", ParticleAttributeFunctions.ParticleDeath, "0.0");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");

        String name = "result_" + nodeId;
        vertexShaderBuilder.addMainLine("// Particle Lifetime Percentage Node");
        vertexShaderBuilder.addMainLine("float" + " " + name + " = (u_time - " + birthOutput.getRepresentation() + ") / (" + deathOutput.getRepresentation() + " - " + birthOutput.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("percentage", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(
            boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput birthOutput = AttributeFunctionUtils.getOutputWithFallbackFragment(inputs, vertexShaderBuilder, fragmentShaderBuilder,
                graphShader, "birth", ParticleAttributeFunctions.ParticleBirth, "0.0");
        FieldOutput deathOutput = AttributeFunctionUtils.getOutputWithFallbackFragment(inputs, vertexShaderBuilder, fragmentShaderBuilder,
                graphShader, "death", ParticleAttributeFunctions.ParticleDeath, "0.0");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");

        String varyingName = "v_" + nodeId + "_lifePercentage";
        vertexShaderBuilder.addMainLine("// Particle Lifetime Percentage Node");
        vertexShaderBuilder.addVaryingVariable(varyingName, "float");
        fragmentShaderBuilder.addVaryingVariable(varyingName, "float");
        vertexShaderBuilder.addMainLine(varyingName + " = (u_time - " + birthOutput.getRepresentation() + ") / (" + deathOutput.getRepresentation() + " - " + birthOutput.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("percentage", new DefaultFieldOutput(ShaderFieldType.Float, varyingName));
    }
}
