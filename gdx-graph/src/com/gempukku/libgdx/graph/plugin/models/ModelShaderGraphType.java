package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.validator.*;

public class ModelShaderGraphType implements ShaderGraphType {
    public static final String TYPE = "Model_Shader";

    private static final PropertyLocation[] propertyLocations = new PropertyLocation[]{PropertyLocation.Uniform, PropertyLocation.Global_Uniform, PropertyLocation.Attribute};
    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ModelShaderConfiguration()};

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

        DAGValidatorWithEndNode dagValidator = new DAGValidatorWithEndNode("end");
        SumGraphValidator sumValidator = new SumGraphValidator(
                new RequiredInputsValidator(nodeConfigurationResolver), new FieldTypeValidator(nodeConfigurationResolver));
        SerialGraphValidator validator = new SerialGraphValidator(dagValidator, sumValidator);
        graphValidator = validator;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public GraphValidator getGraphValidator() {
        return graphValidator;
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return propertyLocations;
    }

    @Override
    public GraphConfiguration[] getConfigurations() {
        return configurations;
    }
}
