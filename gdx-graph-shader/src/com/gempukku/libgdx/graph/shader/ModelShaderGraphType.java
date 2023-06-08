package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.builder.ModelGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.validator.*;

public class ModelShaderGraphType implements ShaderGraphType {
    public static final String TYPE = "Model_Shader";

    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new ModelShaderConfiguration(), new PropertyShaderConfiguration()};

    private GraphValidator graphValidator;

    public ModelShaderGraphType() {
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
                new RequiredValidator(nodeConfigurationResolver),
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
    public GraphShader buildGraphShader(String tag, PipelineRendererConfiguration configuration, GraphWithProperties graph, boolean designTime) {
        return new ModelGraphShaderRecipe().buildGraphShader(tag, designTime, graph, configuration);
    }
}
