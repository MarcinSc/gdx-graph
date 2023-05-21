package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.builder.ParticlesGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.validator.*;

public class ParticleEffectGraphType implements ShaderGraphType {
    public static final String TYPE = "Particle_Effect";

    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{
            new ModelShaderConfiguration(), new PropertyShaderConfiguration(),
            new ParticlesShaderConfiguration()};
    private GraphValidator graphValidator;

    public ParticleEffectGraphType() {
        BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver =
                new BiFunction<String, JsonValue, NodeConfiguration>() {
                    @Override
                    public NodeConfiguration evaluate(String value1, JsonValue value2) {
                        for (GraphConfiguration configuration : configurations) {
                            GraphShaderNodeBuilder graphShaderNodeBuilder = configuration.getGraphShaderNodeBuilder(value1);
                            if (graphShaderNodeBuilder != null) {
                                return graphShaderNodeBuilder.getConfiguration(value2);
                            }
                        }
                        return null;
                    }
                };

        DAGValidatorWithEndNode dagValidator = new DAGValidatorWithEndNode();
        SumGraphValidator sumValidator = new SumGraphValidator(
                new RequiredInputsValidator(nodeConfigurationResolver),
                new MultipleConnectionsValidator(nodeConfigurationResolver),
                new FieldTypeValidator(nodeConfigurationResolver));
        SerialGraphValidator validator = new SerialGraphValidator(dagValidator, sumValidator);
        graphValidator = validator;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getStartNodeIdForValidation() {
        return "end";
    }

    @Override
    public GraphValidator getGraphValidator() {
        return graphValidator;
    }

    @Override
    public GraphConfiguration[] getConfigurations() {
        return configurations;
    }

    @Override
    public GraphShader buildGraphShader(String tag, FileHandleResolver assetResolver, GraphWithProperties graph, boolean designTime) {
        return new ParticlesGraphShaderRecipe().buildGraphShader(tag, designTime, graph, assetResolver);
    }
}
