package com.gempukku.libgdx.graph.plugin.boneanimation.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.IntegerBoxPart;
import com.kotcrab.vis.ui.util.Validators;

public class BoneWeightPropertyBoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return BoneWeightFieldType.type;
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, BoneWeightFieldType.type, location, propertyLocations);
        result.addPropertyBoxPart(new IntegerBoxPart("Max bone weights", "maxBoneWeightCount", 4, new Validators.GreaterThanValidator(0)));
        result.initialize(jsonObject);

        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations) {
        return createPropertyBox(skin, "New Float", null, null, propertyLocations);
    }

}
