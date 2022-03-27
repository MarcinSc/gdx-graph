package com.gempukku.libgdx.graph.pipeline.producer.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.AbstractPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;

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
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes) {
        final String propertyName = data.getString("name");
        final String fieldType = data.getString("type");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final PropertyFieldOutput fieldOutput = new PropertyFieldOutput(fieldType, propertyName);
        result.put("value", fieldOutput);

        return new AbstractPipelineNode(result) {
            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                fieldOutput.setPipelinePropertySource(pipelineDataProvider.getPipelinePropertySource());
            }

            @Override
            public void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs) {

            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {

            }
        };
    }

    private static class PropertyFieldOutput implements PipelineNode.FieldOutput {
        private PipelinePropertySource pipelinePropertySource;
        private final String fieldType;
        private final String propertyName;

        public PropertyFieldOutput(String fieldType, String propertyName) {
            this.fieldType = fieldType;
            this.propertyName = propertyName;
        }

        public void setPipelinePropertySource(PipelinePropertySource pipelinePropertySource) {
            this.pipelinePropertySource = pipelinePropertySource;
        }

        @Override
        public String getPropertyType() {
            return fieldType;
        }

        @Override
        public Object getValue() {
            return pipelinePropertySource.getPipelineProperty(propertyName).getValue();
        }
    }
}
