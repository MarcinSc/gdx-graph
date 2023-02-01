package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;


public class PropertyShaderGraphBoxProducer implements GraphBoxProducer {
    private Array<PropertyGraphBoxCustomization> customizations = new Array<>();

    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return "Property";
    }

    @Override
    public String getMenuLocation() {
        return null;
    }

    public void addPropertyGraphBoxCustomization(PropertyGraphBoxCustomization customization) {
        customizations.add(customization);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");
        PropertyNodeConfiguration configuration = new PropertyNodeConfiguration(name, propertyType);
        GraphBoxImpl result = new GraphBoxImpl(id, configuration) {
            @Override
            public JsonValue getData() {
                JsonValue result = super.getData();
                if (result == null)
                    result = new JsonValue(JsonValue.ValueType.object);
                result.addChild("name", new JsonValue(name));
                result.addChild("type", new JsonValue(propertyType));
                return result;
            }
        };
        result.addOutputGraphPart(new ValueGraphNodeOutput(name, propertyType));

        ShaderFieldType shaderFieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        for (PropertyGraphBoxCustomization customization : customizations) {
            customization.process(shaderFieldType, configuration, result, data);
        }

        return result;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return null;
    }
}
