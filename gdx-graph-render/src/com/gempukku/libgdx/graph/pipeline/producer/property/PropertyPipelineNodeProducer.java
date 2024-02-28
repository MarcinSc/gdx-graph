package com.gempukku.libgdx.graph.pipeline.producer.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.AbstractPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

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
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration pipelineRendererConfiguration) {
        final String propertyName = data.getString("name");

        PipelineProperty pipelineProperty = pipelineRendererConfiguration.getPipelinePropertySource().getPipelineProperty(propertyName);
        PropertyContainer propertyContainer = pipelineRendererConfiguration.getPipelinePropertyContainer();

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final PropertyFieldOutput fieldOutput = new PropertyFieldOutput(
                propertyName, pipelineProperty, propertyContainer);
        result.put("value", fieldOutput);

        return new AbstractPipelineNode(result) {
            @Override
            public void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs) {

            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

            }
        };
    }

    private static class PropertyFieldOutput implements PipelineNode.FieldOutput {
        private final String propertyName;
        private final PipelineProperty pipelineProperty;
        private final PropertyContainer pipelinePropertyContainer;

        public PropertyFieldOutput(
                String propertyName,
                PipelineProperty pipelineProperty, PropertyContainer pipelinePropertyContainer) {
            this.propertyName = propertyName;
            this.pipelineProperty = pipelineProperty;
            this.pipelinePropertyContainer = pipelinePropertyContainer;
        }

        @Override
        public String getPropertyType() {
            return pipelineProperty.getType();
        }

        @Override
        public Object getValue() {
            Object value = pipelinePropertyContainer.getValue(propertyName);
            if (value != null)
                return value;
            return pipelineProperty.getValue();
        }
    }
}
