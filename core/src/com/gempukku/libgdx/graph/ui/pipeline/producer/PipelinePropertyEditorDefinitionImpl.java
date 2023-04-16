package com.gempukku.libgdx.graph.ui.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;

import java.util.LinkedList;
import java.util.List;

public class PipelinePropertyEditorDefinitionImpl implements PropertyEditorDefinition {
    private String defaultName;
    private String type;
    private List<Supplier<GraphNodeEditorPart>> propertyBoxParts = new LinkedList<>();
    private Array<PropertyGraphBoxCustomization> customizations = new Array<>();

    public PipelinePropertyEditorDefinitionImpl(String defaultName, String type) {
        this.defaultName = defaultName;
        this.type = type;
    }

    public void addCustomization(PropertyGraphBoxCustomization customization) {
        customizations.add(customization);
    }

    public void addPropertyBoxPart(Supplier<GraphNodeEditorPart> propertyBoxPart) {
        propertyBoxParts.add(propertyBoxPart);
    }

    @Override
    public String getDefaultName() {
        return defaultName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Array<PropertyGraphBoxCustomization> getCustomizations() {
        return customizations;
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, type, null, propertyLocations);
        for (Supplier<GraphNodeEditorPart> propertyBoxPart : propertyBoxParts) {
            result.addPropertyBoxPart(propertyBoxPart.get());
        }
        result.initialize(jsonObject);

        return result;
    }
}
