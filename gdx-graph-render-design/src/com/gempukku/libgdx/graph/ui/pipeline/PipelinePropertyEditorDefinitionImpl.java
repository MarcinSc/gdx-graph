package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;

import java.util.LinkedList;
import java.util.List;

public class PipelinePropertyEditorDefinitionImpl implements PropertyEditorDefinition {
    private String defaultName;
    private String type;
    private List<Supplier<GraphNodeEditorPart>> propertyEditorParts = new LinkedList<>();
    private Array<PropertyGraphEditorCustomization> customizations = new Array<>();

    public PipelinePropertyEditorDefinitionImpl(String defaultName, String type) {
        this.defaultName = defaultName;
        this.type = type;
    }

    public void addCustomization(PropertyGraphEditorCustomization customization) {
        customizations.add(customization);
    }

    public void addPropertyEditorPart(Supplier<GraphNodeEditorPart> propertyEditorPart) {
        propertyEditorParts.add(propertyEditorPart);
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
    public Array<PropertyGraphEditorCustomization> getCustomizations() {
        return customizations;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, JsonValue data) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, type);
        for (Supplier<GraphNodeEditorPart> propertyEditorPart : propertyEditorParts) {
            result.addPropertyEditorPart(propertyEditorPart.get());
        }
        result.initialize(data);

        return result;
    }
}
