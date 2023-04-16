package com.gempukku.libgdx.graph.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.DefaultGraphProperty;
import com.gempukku.libgdx.graph.data.DefaultGraphWithProperties;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeGroup;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphConnection;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNode;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GraphSerializer {
    private static final int VERSION_MAJOR = 0;
    private static final int VERSION_MINOR = 5;
    private static final int VERSION_PATCH = 0;
    public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;

    public static GraphWithProperties deserializeGraphWithProperties(JsonValue graph, String graphType) {
        // Assuming default
        String version = graph.has("version") ? graph.getString("version") : "0.1.0";
        if (!canReadVersion(version)) {
            throw new IllegalArgumentException("Unable to read a graph of version " + version);
        }
        if (!VERSION.equals(version))
            Gdx.app.debug("GraphLoader", "Reading a graph from different version " + VERSION + " != " + version);

        String loadedGraphType = graph.getString("type", null);
        if (loadedGraphType != null && !graphType.equals(loadedGraphType)) {
            throw new IllegalArgumentException("Unable to read a graph - graph type does not match: " + graphType + " != " + loadedGraphType);
        }

        DefaultGraphWithProperties result = new DefaultGraphWithProperties(loadedGraphType);

        for (JsonValue object : graph.get("nodes")) {
            String type = object.getString("type");
            String id = object.getString("id");
            float x = object.getFloat("x");
            float y = object.getFloat("y");
            JsonValue data = object.get("data");
            result.addGraphNode(new DefaultGraphNode(id, type, x, y, data));
        }
        for (JsonValue connection : graph.get("connections")) {
            String fromNode = connection.getString("fromNode");
            String fromField = connection.getString("fromField");
            String toNode = connection.getString("toNode");
            String toField = connection.getString("toField");
            result.addGraphConnection(new DefaultGraphConnection(fromNode, fromField, toNode, toField));
        }
        for (JsonValue property : graph.get("properties")) {
            String type = property.getString("type");
            String name = property.getString("name");
            JsonValue data = property.get("data");
            String location = property.getString("location", null);
            PropertyLocation resolvedLocation = location != null ? PropertyLocation.valueOf(location) : null;
            result.addGraphProperty(new DefaultGraphProperty(name, type, resolvedLocation, data));
        }
        JsonValue groups = graph.get("groups");
        if (groups != null) {
            for (JsonValue group : groups) {
                String name = group.getString("name");
                JsonValue nodes = group.get("nodes");
                ObjectSet<String> nodeIds = new ObjectSet<>();
                for (JsonValue node : nodes) {
                    nodeIds.add(node.asString());
                }
                result.addNodeGroup(new DefaultNodeGroup(name, nodeIds));
            }
        }
        return result;
    }

    public static JsonValue serializeGraphWithProperties(GraphWithProperties graph) {
        JsonValue result = serializeGraph(graph);

        JsonValue properties = new JsonValue(JsonValue.ValueType.array);
        for (GraphProperty propertyBox : graph.getProperties()) {
            JsonValue property = new JsonValue(JsonValue.ValueType.object);
            property.addChild("name", new JsonValue(propertyBox.getName()));
            property.addChild("type", new JsonValue(propertyBox.getType()));
            PropertyLocation location = propertyBox.getLocation();
            if (location != null)
                property.addChild("location", new JsonValue(location.name()));

            JsonValue data = propertyBox.getData();
            if (data != null)
                property.addChild("data", data);

            properties.addChild(property);
        }
        result.addChild("properties", properties);

        return result;
    }

    private static JsonValue serializeGraph(Graph graph) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("version", new JsonValue(VERSION));
        result.addChild("type", new JsonValue(graph.getType()));

        JsonValue objects = new JsonValue(JsonValue.ValueType.array);
        for (JsonValue jsonValue : getSortedNodesAsJson(graph)) {
            objects.addChild(jsonValue);
        }
        result.addChild("nodes", objects);

        List<JsonValue> connectionJsonValues = getSortedConnectionsAsJson(graph);
        JsonValue connections = new JsonValue(JsonValue.ValueType.array);
        for (JsonValue connection : connectionJsonValues) {
            connections.addChild(connection);
        }
        result.addChild("connections", connections);

        JsonValue groups = new JsonValue(JsonValue.ValueType.array);
        for (NodeGroup nodeGroup : graph.getGroups()) {
            JsonValue group = new JsonValue(JsonValue.ValueType.object);
            group.addChild("name", new JsonValue(nodeGroup.getName()));
            JsonValue nodes = new JsonValue(JsonValue.ValueType.array);
            for (String nodeId : nodeGroup.getNodeIds()) {
                nodes.addChild(new JsonValue(nodeId));
            }
            group.addChild("nodes", nodes);
            groups.addChild(group);
        }
        result.addChild("groups", groups);
        return result;
    }

    private static List<JsonValue> getSortedConnectionsAsJson(Graph graph) {
        List<JsonValue> connectionJsonValues = new ArrayList<>();
        for (GraphConnection connection : graph.getConnections()) {
            JsonValue conn = new JsonValue(JsonValue.ValueType.object);
            conn.addChild("fromNode", new JsonValue(connection.getNodeFrom()));
            conn.addChild("fromField", new JsonValue(connection.getFieldFrom()));
            conn.addChild("toNode", new JsonValue(connection.getNodeTo()));
            conn.addChild("toField", new JsonValue(connection.getFieldTo()));
            connectionJsonValues.add(conn);
        }
        // Sort the connections
        Collections.sort(connectionJsonValues, new Comparator<JsonValue>() {
            @Override
            public int compare(JsonValue o1, JsonValue o2) {
                String s1 = getNodeString(o1);
                String s2 = getNodeString(o2);
                return s1.compareTo(s2);
            }

            private String getNodeString(JsonValue node) {
                return node.getString("fromNode") + "." + node.getString("fromField") + "." + node.getString("toNode") + "." + node.getString("toField");
            }
        });
        return connectionJsonValues;
    }

    private static List<JsonValue> getSortedNodesAsJson(Graph graph) {
        List<JsonValue> nodeList = new ArrayList<>();
        Vector2 min = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for (GraphNode node : graph.getNodes()) {
            min.set(Math.min(min.x, node.getX()), Math.min(min.y, node.getY()));
        }
        for (GraphNode graphNode : graph.getNodes()) {
            JsonValue object = new JsonValue(JsonValue.ValueType.object);
            object.addChild("id", new JsonValue(graphNode.getId()));
            object.addChild("type", new JsonValue(graphNode.getType()));
            object.addChild("x", new JsonValue(graphNode.getX() - min.x));
            object.addChild("y", new JsonValue(graphNode.getY() - min.y));

            JsonValue data = graphNode.getData();
            if (data != null)
                object.addChild("data", data);

            nodeList.add(object);
        }
        // Sort the nodes
        Collections.sort(nodeList, new Comparator<JsonValue>() {
            @Override
            public int compare(JsonValue o1, JsonValue o2) {
                return o1.getString("id").compareTo(o2.getString("id"));
            }
        });
        return nodeList;
    }

    private static boolean canReadVersion(String version) {
        String[] split = version.split("\\.");
        if (split.length != 3)
            return false;

        int major = Integer.parseInt(split[0]);
        int minor = Integer.parseInt(split[1]);
        int patch = Integer.parseInt(split[2]);

        // Breaking compatibility on major version
        if (major != VERSION_MAJOR)
            return false;
        // Should support every previous minor version
        if (minor > VERSION_MINOR)
            return false;
        // Should support same minor if previous patch version
        return minor != VERSION_MINOR || patch <= VERSION_PATCH;
    }
}
