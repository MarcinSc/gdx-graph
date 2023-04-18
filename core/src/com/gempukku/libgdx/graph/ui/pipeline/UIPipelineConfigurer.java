package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyEditorDefinitionImpl;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.editor.part.*;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.ArrayList;
import java.util.List;

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

        DefaultMenuNodeConfiguration nodeConfiguration = new DefaultMenuNodeConfiguration(producerType, producerName, menuLocation);
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
                DefaultGraphNodeInput nodeInput = new DefaultGraphNodeInput(id, name, required, mainConnection, acceptMultiple, types);
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
                DefaultGraphNodeOutput nodeOutput = new DefaultGraphNodeOutput(id, name, mainConnection, validationFunction, types);
                nodeConfiguration.addNodeOutput(nodeOutput);
            }
        }
        final JsonValue fields = boxProducer.get("fields");
        GdxGraphNodeEditorProducer producer = new GdxGraphNodeEditorProducer(nodeConfiguration) {
            @Override
            protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
                if (fields != null) {
                    for (JsonValue field : fields) {
                        String fieldType = field.getString("type");
                        GraphNodeEditorPart fieldGraphBoxPart = createFieldGraphBoxPart(fieldType, field);
                        graphNodeEditor.addGraphBoxPart(fieldGraphBoxPart);
                    }
                }
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
        PipelinePropertyEditorDefinitionImpl producer = new PipelinePropertyEditorDefinitionImpl(defaultName, typeName);

        JsonValue fields = propertyType.get("fields");
        if (fields != null) {
            for (final JsonValue field : fields) {
                final String fieldType = field.getString("type");
                producer.addPropertyBoxPart(
                        new Supplier<GraphNodeEditorPart>() {
                            @Override
                            public GraphNodeEditorPart get() {
                                GraphNodeEditorPart propertyBoxPart = createFieldGraphBoxPart(fieldType, field);
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

    private static GraphNodeEditorPart createFieldGraphBoxPart(String fieldType, JsonValue field) {
        switch (fieldType) {
            case PipelineFieldType.Float: {
                return new FloatEditorPart(field.getString("label") + " ", field.getString("property"),
                        field.getFloat("defaultValue", 0), null);
            }
            case PipelineFieldType.Vector2: {
                JsonValue properties = field.get("properties");
                JsonValue defaultValues = field.get("defaultValues");
                return new Vector2EditorPart(field.getString("label") + " ",
                        properties.getString(0), properties.getString(1),
                        defaultValues.getFloat(0), defaultValues.getFloat(1),
                        null, null);
            }
            case PipelineFieldType.Vector3: {
                JsonValue properties = field.get("properties");
                JsonValue defaultValues = field.get("defaultValues");
                return new Vector3EditorPart(field.getString("label") + " ",
                        properties.getString(0), properties.getString(1), properties.getString(2),
                        defaultValues.getFloat(0), defaultValues.getFloat(1), defaultValues.getFloat(2),
                        null, null, null);
            }
            case PipelineFieldType.Color: {
                return new ColorEditorPart(field.getString("label") + " ", field.getString("property"),
                        Color.valueOf(field.getString("defaultValue")));
            }
            case PipelineFieldType.Boolean: {
                return new CheckboxEditorPart(field.getString("label") + " ", field.getString("property"),
                        field.getBoolean("defaultValue"));
            }
            case "String": {
                return new StringEditorPart(field.getString("label") + " ", field.getString("property"));
            }
            case "Comment": {
                VisLabel description = new VisLabel(field.getString("text"));
                description.setColor(Color.valueOf(field.getString("color", "FFFFFFFF")));
                return new DefaultGraphNodeEditorPart(description, null);
            }
        }
        return null;
    }
}
