package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.ui.graph.data.Graph;
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
        return graphValidator.validateSubGraph(graph, "end");
    }
}
