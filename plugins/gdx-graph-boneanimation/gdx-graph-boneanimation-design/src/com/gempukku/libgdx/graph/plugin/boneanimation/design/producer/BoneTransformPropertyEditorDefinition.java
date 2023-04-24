package com.gempukku.libgdx.graph.plugin.boneanimation.design.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneTransformFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.IntegerEditorPart;
import com.kotcrab.vis.ui.util.Validators;

public class BoneTransformPropertyEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return BoneTransformFieldType.type;
    }

    @Override
    public String getDefaultName() {
        return "New Bone Transform";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, BoneTransformFieldType.type, location, propertyLocations);
        result.addPropertyBoxPart(new IntegerEditorPart("Max bone count", "maxBoneCount", 12, new Validators.GreaterThanValidator(0)));
        result.initialize(jsonObject);

        return result;
    }
}
