package com.gempukku.libgdx.graph.plugin.boneanimation.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
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
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, BoneWeightFieldType.type, location, propertyLocations);
        result.addPropertyBoxPart(new IntegerEditorPart("Max bone weights", "maxBoneWeightCount", 4, new Validators.GreaterThanValidator(0)));
        result.initialize(jsonObject);

        return result;
    }
}
