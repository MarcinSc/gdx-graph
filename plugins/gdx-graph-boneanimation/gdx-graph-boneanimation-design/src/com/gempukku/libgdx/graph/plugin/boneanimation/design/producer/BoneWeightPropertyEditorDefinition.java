package com.gempukku.libgdx.graph.plugin.boneanimation.design.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.IntegerEditorPart;
import com.kotcrab.vis.ui.util.Validators;

public class BoneWeightPropertyEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return BoneWeightFieldType.type;
    }

    @Override
    public String getDefaultName() {
        return "New Bone Weight";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, BoneWeightFieldType.type, location, propertyLocations);
        result.addPropertyEditorPart(new IntegerEditorPart("Max bone weights", "maxBoneWeightCount", 4, new Validators.GreaterThanValidator(0)));
        result.initialize(jsonObject);

        return result;
    }
}
