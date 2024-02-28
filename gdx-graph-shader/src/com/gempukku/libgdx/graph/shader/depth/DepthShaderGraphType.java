package com.gempukku.libgdx.graph.shader.depth;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.builder.DepthGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.validator.*;

public class DepthShaderGraphType implements ShaderGraphType {
    public static final String TYPE = "Shadow_Shader";

    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new ModelShaderConfiguration(), new PropertyShaderConfiguration()};
    private GraphValidator graphValidator;

    public DepthShaderGraphType() {
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

        DAGValidator dagValidator = new DAGValidator();
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
    public GraphValidationResult validateGraph(Graph graph) {
        return validateSubGraph(graph, "end");
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startingNode) {
        return graphValidator.validateSubGraph(graph, startingNode);
    }

    @Override
    public GraphConfiguration[] getConfigurations() {
        return configurations;
    }

    @Override
    public GraphShader buildGraphShader(String tag, PipelineRendererConfiguration configuration, GraphWithProperties graph, boolean designTime) {
        return new DepthGraphShaderRecipe().buildGraphShader(tag, designTime, graph, configuration);
    }
}
