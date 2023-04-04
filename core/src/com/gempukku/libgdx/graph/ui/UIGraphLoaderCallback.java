package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.loader.GraphLoaderCallback;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTable;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

public class UIGraphLoaderCallback implements GraphLoaderCallback<GraphDesignTable> {
    private Skin skin;
    private GraphDesignTable graphDesignTable;
    private PropertyLocation[] propertyLocations;
    private UIGraphConfiguration[] uiGraphConfigurations;

    public UIGraphLoaderCallback(Skin skin, GraphDesignTable graphDesignTable,
                                 PropertyLocation[] propertyLocations, UIGraphConfiguration... uiGraphConfiguration) {
        this.skin = skin;
        this.graphDesignTable = graphDesignTable;
        this.propertyLocations = propertyLocations;
        this.uiGraphConfigurations = uiGraphConfiguration;
    }

    @Override
    public void start() {
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JsonValue data) {
        GraphBoxProducer producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphDesignTable.getGraphContainer().addGraphBox(graphBox, producer.getName(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphDesignTable.getGraphContainer().addGraphConnection(fromNode, fromProperty, toNode, toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, PropertyLocation location, JsonValue data) {
        PropertyBoxProducer producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox propertyBox = producer.createPropertyBox(skin, name, location, data, propertyLocations);
        graphDesignTable.addPropertyBox(type, propertyBox);
    }

    @Override
    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        graphDesignTable.getGraphContainer().addNodeGroup(name, nodeIds);
    }

    @Override
    public GraphDesignTable end() {
        graphDesignTable.finishedLoading();
        return graphDesignTable;
    }

    private PropertyBoxProducer findPropertyProducerByType(String type) {
        for (UIGraphConfiguration uiGraphConfiguration : uiGraphConfigurations) {
            for (PropertyBoxProducer producer : uiGraphConfiguration.getPropertyBoxProducers().values()) {
                if (producer.getType().equals(type))
                    return producer;
            }
        }

        return null;
    }

    private GraphBoxProducer findProducerByType(String type) {
        for (UIGraphConfiguration uiGraphConfiguration : uiGraphConfigurations) {
            for (GraphBoxProducer graphBoxProducer : uiGraphConfiguration.getGraphBoxProducers()) {
                if (graphBoxProducer.getType().equals(type))
                    return graphBoxProducer;
            }
        }

        return null;
    }
}
