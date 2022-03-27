package com.gempukku.libgdx.graph.loader;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.*;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public abstract class GraphDataLoaderCallback<T, U extends FieldType> implements GraphLoaderCallback<T>, Graph<GraphNode, GraphConnection, GraphProperty> {
    private final ObjectMap<String, GraphNodeData> graphNodes = new ObjectMap<>();
    private final Array<GraphConnectionData> graphConnections = new Array<>();
    private final ObjectMap<String, GraphPropertyData> graphProperties = new ObjectMap<>();

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JsonValue data) {
        graphNodes.put(id, new GraphNodeData(id, getNodeConfiguration(type, data), data));
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromField, String toNode, String toField) {
        graphConnections.add(new GraphConnectionData(fromNode, fromField, toNode, toField));
    }

    @Override
    public void addPipelineProperty(String type, String name, PropertyLocation location, JsonValue data) {
        graphProperties.put(name, new GraphPropertyData(name, type, data, location));
    }

    @Override
    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        // Ignore - used only in UI
    }

    @Override
    public GraphNodeData getNodeById(String id) {
        return graphNodes.get(id);
    }

    @Override
    public GraphPropertyData getPropertyByName(String name) {
        return graphProperties.get(name);
    }

    @Override
    public Iterable<? extends GraphConnectionData> getConnections() {
        return graphConnections;
    }

    @Override
    public Iterable<? extends GraphNodeData> getNodes() {
        return graphNodes.values();
    }

    @Override
    public Iterable<? extends GraphPropertyData> getProperties() {
        return graphProperties.values();
    }

    protected abstract NodeConfiguration getNodeConfiguration(String type, JsonValue data);

    private static class GraphNodeData implements GraphNode {
        private final String id;
        private final NodeConfiguration configuration;
        private final JsonValue data;

        public GraphNodeData(String id, NodeConfiguration configuration, JsonValue data) {
            this.id = id;
            this.configuration = configuration;
            this.data = data;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public JsonValue getData() {
            return data;
        }

        @Override
        public NodeConfiguration getConfiguration() {
            return configuration;
        }
    }

    private static class GraphPropertyData implements GraphProperty {
        private final String name;
        private final String type;
        private final JsonValue data;
        private final PropertyLocation location;

        public GraphPropertyData(String name, String type, JsonValue data, PropertyLocation location) {
            this.name = name;
            this.type = type;
            this.data = data;
            this.location = location;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public JsonValue getData() {
            return data;
        }

        @Override
        public PropertyLocation getLocation() {
            return location;
        }
    }

    private static class GraphConnectionData implements GraphConnection {
        private final String nodeFrom;
        private final String fieldFrom;
        private final String nodeTo;
        private final String fieldTo;

        public GraphConnectionData(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
            this.nodeFrom = nodeFrom;
            this.fieldFrom = fieldFrom;
            this.nodeTo = nodeTo;
            this.fieldTo = fieldTo;
        }

        @Override
        public String getNodeFrom() {
            return nodeFrom;
        }

        @Override
        public String getFieldFrom() {
            return fieldFrom;
        }

        @Override
        public String getNodeTo() {
            return nodeTo;
        }

        @Override
        public String getFieldTo() {
            return fieldTo;
        }
    }
}
