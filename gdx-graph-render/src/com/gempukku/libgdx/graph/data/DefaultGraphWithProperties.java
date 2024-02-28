package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.NodeGroup;
import com.gempukku.libgdx.graph.data.impl.DefaultGraph;

public class DefaultGraphWithProperties implements GraphWithProperties {
    private DefaultGraph<GraphNode, GraphConnection, NodeGroup> graph;
    private Array<GraphProperty> properties = new Array<>();

    public DefaultGraphWithProperties(String type) {
        graph = new DefaultGraph<>(type);
    }

    public void addGraphProperty(GraphProperty graphProperty) {
        properties.add(graphProperty);
    }

    public void addGraphNode(GraphNode graphNode) {
        graph.addGraphNode(graphNode);
    }

    public void addGraphConnection(GraphConnection graphConnection) {
        graph.addGraphConnection(graphConnection);
    }

    public void addNodeGroup(NodeGroup nodeGroup) {
        graph.addNodeGroup(nodeGroup);
    }

    @Override
    public String getType() {
        return graph.getType();
    }

    @Override
    public Iterable<? extends GraphProperty> getProperties() {
        return properties;
    }

    @Override
    public GraphNode getNodeById(String nodeId) {
        return graph.getNodeById(nodeId);
    }

    @Override
    public Iterable<? extends GraphNode> getNodes() {
        return graph.getNodes();
    }

    @Override
    public Iterable<? extends GraphConnection> getConnections() {
        return graph.getConnections();
    }

    @Override
    public Iterable<? extends NodeGroup> getGroups() {
        return graph.getGroups();
    }
}
