package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.validator.*;

public class RenderPipelineGraphType implements GraphType {
    public static final String TYPE = "Render_Pipeline";

    private final GraphValidator graphValidator;

    public RenderPipelineGraphType() {
        BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver =
                new BiFunction<String, JsonValue, NodeConfiguration>() {
                    @Override
                    public NodeConfiguration evaluate(String type, JsonValue data) {
                        PipelineNodeProducer producer = RendererPipelineConfiguration.findProducer(type);
                        if (producer == null)
                            throw new GdxRuntimeException("Unable to find producer for type: " + type);
                        return producer.getConfiguration(data);
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
}
