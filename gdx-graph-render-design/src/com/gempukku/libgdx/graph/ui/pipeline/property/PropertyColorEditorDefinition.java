package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.ColorPickerSupplier;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.ColorEditorPart;


public class PropertyColorEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return PipelineFieldType.Color;
    }

    @Override
    public String getDefaultName() {
        return "New Color";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, getType());
        result.addPropertyEditorPart(new ColorEditorPart(ColorPickerSupplier.instance, "Color", "color", Color.WHITE, "gdx-graph-property-label"));
        result.initialize(jsonObject);

        return result;
    }
}
