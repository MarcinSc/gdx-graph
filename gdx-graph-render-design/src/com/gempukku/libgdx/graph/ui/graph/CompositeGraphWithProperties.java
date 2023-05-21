package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeGroup;

public class CompositeGraphWithProperties implements GraphWithProperties {
    private Graph graph;
    private Iterable<? extends GraphProperty> properties;

    public CompositeGraphWithProperties(Graph graph, Iterable<? extends GraphProperty> properties) {
        this.graph = graph;
        this.properties = properties;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setProperties(Iterable<? extends GraphProperty> properties) {
        this.properties = properties;
    }

    @Override
    public Iterable<? extends GraphProperty> getProperties() {
        return properties;
    }

    @Override
    public String getType() {
        return graph.getType();
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
