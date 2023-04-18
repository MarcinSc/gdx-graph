package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;


public class PropertyGraphNodeEditorProducer implements MenuGraphNodeEditorProducer {
    private UIGraphConfiguration[] configurations;

    public PropertyGraphNodeEditorProducer(UIGraphConfiguration... configurations) {
        this.configurations = configurations;
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

    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public DefaultNodeConfiguration getConfiguration(JsonValue data) {
        return new DefaultNodeConfiguration(data.getString("type"), data.getString("name"));
    }

    @Override
    public DefaultGraphNodeEditor createNodeEditor(Skin skin, JsonValue data) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");
        DefaultGraphNodeEditor result = new DefaultGraphNodeEditor(new PropertyNodeConfiguration(name, propertyType)) {
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
        for (UIGraphConfiguration configuration : configurations) {
            PropertyEditorDefinition propertyEditorDefinition = configuration.getPropertyEditorDefinitions().get(propertyType);
            if (propertyEditorDefinition != null) {
                Iterable<? extends PropertyGraphBoxCustomization> customizations = propertyEditorDefinition.getCustomizations();
                if (customizations != null) {
                    for (PropertyGraphBoxCustomization customization : customizations) {
                        customization.process(getConfiguration(data), result, data);
                    }
                }
            }
        }

        return result;
    }
}
