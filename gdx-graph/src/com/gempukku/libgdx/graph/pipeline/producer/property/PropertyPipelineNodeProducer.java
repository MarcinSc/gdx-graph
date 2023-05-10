package com.gempukku.libgdx.graph.pipeline.producer.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final String fieldType = data.getString("type");
        return new PropertyNodeConfiguration(name, fieldType);
    }

    @Override
    public ObjectMap<String, String> getOutputTypes(JsonValue data, ObjectMap<String, Array<String>> inputTypes) {
        ObjectMap<String, String> result = new ObjectMap<>();
        result.put("value", data.getString("type"));
        return result;
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes, final PipelineDataProvider pipelineDataProvider) {
        final String propertyName = data.getString("name");
        final String fieldType = data.getString("type");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final PropertyFieldOutput fieldOutput = new PropertyFieldOutput(
                pipelineDataProvider.getPipelinePropertySource(), pipelineDataProvider.getRootPropertyContainer(),
                fieldType, propertyName);
        result.put("value", fieldOutput);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

            }
        };
    }

    private static class PropertyFieldOutput implements PipelineNode.FieldOutput {
        private final PipelinePropertySource pipelinePropertySource;
        private final PropertyContainer pipelinePropertyContainer;
        private final String fieldType;
        private final String propertyName;

        public PropertyFieldOutput(
                PipelinePropertySource pipelinePropertySource, PropertyContainer pipelinePropertyContainer,
                                   String fieldType, String propertyName) {
            this.pipelinePropertySource = pipelinePropertySource;
            this.pipelinePropertyContainer = pipelinePropertyContainer;
            this.fieldType = fieldType;
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyType() {
            return fieldType;
        }

        @Override
        public Object getValue() {
            Object value = pipelinePropertyContainer.getValue(propertyName);
            if (value != null)
                return value;
            return pipelinePropertySource.getPipelineProperty(propertyName).getValue();
        }
    }
}
