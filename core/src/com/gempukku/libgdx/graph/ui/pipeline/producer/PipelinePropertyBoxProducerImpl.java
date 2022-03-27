package com.gempukku.libgdx.graph.ui.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PipelinePropertyBoxProducerImpl implements PropertyBoxProducer {
    private String defaultName;
    private String type;
    private List<Supplier<PropertyBoxPart>> propertyBoxParts = new LinkedList<>();

    public PipelinePropertyBoxProducerImpl(String defaultName, String type) {
        this.defaultName = defaultName;
        this.type = type;
    }

    public void addPropertyBoxPart(Supplier<PropertyBoxPart> propertyBoxPart) {
        propertyBoxParts.add(propertyBoxPart);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, type, null, propertyLocations);
        for (Supplier<PropertyBoxPart> propertyBoxPart : propertyBoxParts) {
            result.addPropertyBoxPart(propertyBoxPart.get());
        }
        result.initialize(jsonObject);

        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations) {
        return createPropertyBox(skin, defaultName, null, null, propertyLocations);
    }
}
