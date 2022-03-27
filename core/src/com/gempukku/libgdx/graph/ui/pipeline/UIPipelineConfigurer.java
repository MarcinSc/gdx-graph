package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.part.*;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class UIPipelineConfigurer {
    public static void processPipelineConfig(JsonValue value) {
        JsonValue propertyTypes = value.get("propertyTypes");
        if (propertyTypes != null) {
            for (JsonValue propertyType : propertyTypes) {
                processPropertyType(propertyType);
            }
        }
        JsonValue boxProducers = value.get("boxProducers");
        if (boxProducers != null) {
            for (JsonValue boxProducer : boxProducers) {
                processBoxProducer(boxProducer);
            }
        }
    }

    private static void processBoxProducer(JsonValue boxProducer) {
        String producerType = boxProducer.name();
        String producerName = boxProducer.getString("name");
        String menuLocation = boxProducer.getString("menuLocation");

        NodeConfigurationImpl nodeConfiguration = new NodeConfigurationImpl(producerType, producerName, menuLocation);
        JsonValue inputs = boxProducer.get("inputs");
        if (inputs != null) {
            for (JsonValue input : inputs) {
                String id = input.getString("id");
                String name = input.getString("name");
                boolean required = input.getBoolean("required", false);
                boolean mainConnection = input.getBoolean("mainConnection", false);
                boolean acceptMultiple = input.getBoolean("acceptMultiple", false);
                JsonValue type = input.get("type");
                String[] types = convertToArrayOfStrings(type);
                GraphNodeInputImpl nodeInput = new GraphNodeInputImpl(id, name, required, mainConnection, acceptMultiple, types);
                nodeConfiguration.addNodeInput(nodeInput);
            }
        }
        JsonValue outputs = boxProducer.get("outputs");
        if (outputs != null) {
            for (JsonValue output : outputs) {
                String id = output.getString("id");
                String name = output.getString("name");
                boolean mainConnection = output.getBoolean("mainConnection", false);
                JsonValue validation = output.get("validation");
                Function<ObjectMap<String, Array<String>>, String> validationFunction = convertToValidationFunction(validation);
                JsonValue type = output.get("type");
                String[] types = convertToArrayOfStrings(type);
                GraphNodeOutputImpl nodeOutput = new GraphNodeOutputImpl(id, name, mainConnection,
                        validationFunction, types);
                nodeConfiguration.addNodeOutput(nodeOutput);
            }
        }
        final JsonValue fields = boxProducer.get("fields");
        GraphBoxProducerImpl producer = new GraphBoxProducerImpl(nodeConfiguration) {
            @Override
            public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
                GraphBoxImpl result = createGraphBox(id);

                if (fields != null) {
                    for (JsonValue field : fields) {
                        String fieldType = field.getString("type");
                        GraphBoxPart fieldGraphBoxPart = createFieldGraphBoxPart(fieldType, field);
                        fieldGraphBoxPart.initialize(data);
                        result.addGraphBoxPart(fieldGraphBoxPart);
                    }
                }

                addConfigurationInputsAndOutputs(result);
                return result;
            }
        };
        UIPipelineConfiguration.register(producer);
    }

    private static Function<ObjectMap<String, Array<String>>, String> convertToValidationFunction(JsonValue validation) {
        if (validation != null) {
            String validationType = validation.getString("type");
            if (validationType.equals("multiParamVectorArithmetic")) {
                return new MultiParamVectorArithmeticOutputTypeFunction(validation.getString("floatType"), validation.getString("inputId"));
            } else if (validationType.equals("vectorArithmetic")) {
                return new VectorArithmeticOutputTypeFunction(validation.getString("floatType"),
                        validation.getString("input1"), validation.getString("input2"));
            }
            throw new IllegalArgumentException("Unable to resolve validation type: " + validationType);
        }
        return null;
    }

    private static String[] convertToArrayOfStrings(JsonValue type) {
        if (type.isArray()) {
            List<String> result = new ArrayList<>();
            for (JsonValue jsonValue : type) {
                result.add(jsonValue.asString());
            }
            return result.toArray(new String[0]);
        } else {
            return new String[]{type.asString()};
        }
    }

    private static void processPropertyType(JsonValue propertyType) {
        final String typeName = propertyType.name();
        final String defaultName = propertyType.getString("defaultName");
        PipelinePropertyBoxProducerImpl producer = new PipelinePropertyBoxProducerImpl(defaultName, typeName);

        JsonValue fields = propertyType.get("fields");
        if (fields != null) {
            for (final JsonValue field : fields) {
                final String fieldType = field.getString("type");
                producer.addPropertyBoxPart(
                        new Supplier<PropertyBoxPart>() {
                            @Override
                            public PropertyBoxPart get() {
                                GraphBoxPart propertyBoxPart = createFieldGraphBoxPart(fieldType, field);
                                if (propertyBoxPart != null)
                                    return propertyBoxPart;
                                throw new IllegalArgumentException("Unable to resolve field type: " + fieldType);
                            }
                        }
                );
            }
        }

        UIPipelineConfiguration.registerPropertyType(producer);
    }

    private static GraphBoxPart createFieldGraphBoxPart(String fieldType, JsonValue field) {
        switch (fieldType) {
            case "Float": {
                return new FloatBoxPart(field.getString("label") + " ", field.getString("property"),
                        field.getFloat("defaultValue", 0), null);
            }
            case "Vector2": {
                JsonValue properties = field.get("properties");
                JsonValue defaultValues = field.get("defaultValues");
                return new Vector2BoxPart(field.getString("label") + " ",
                        properties.getString(0), properties.getString(1),
                        defaultValues.getFloat(0), defaultValues.getFloat(1),
                        null, null);
            }
            case "Vector3": {
                JsonValue properties = field.get("properties");
                JsonValue defaultValues = field.get("defaultValues");
                return new Vector3BoxPart(field.getString("label") + " ",
                        properties.getString(0), properties.getString(1), properties.getString(2),
                        defaultValues.getFloat(0), defaultValues.getFloat(1), defaultValues.getFloat(2),
                        null, null, null);
            }
            case "Color": {
                return new ColorBoxPart(field.getString("label") + " ", field.getString("property"),
                        Color.valueOf(field.getString("defaultValue")));
            }
            case "Boolean": {
                return new CheckboxBoxPart(field.getString("label") + " ", field.getString("property"),
                        field.getBoolean("defaultValue"));
            }
            case "String": {
                return new StringBoxPart(field.getString("label") + " ", field.getString("property"));
            }
            case "Comment": {
                VisLabel description = new VisLabel(field.getString("text"));
                description.setColor(Color.valueOf(field.getString("color", "FFFFFFFF")));
                return new GraphBoxPartImpl(description, null);
            }
        }
        return null;
    }
}
