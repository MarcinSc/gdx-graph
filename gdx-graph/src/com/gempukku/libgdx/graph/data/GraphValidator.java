package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class GraphValidator<T extends GraphNode, U extends GraphConnection, V extends GraphProperty> {
    public ValidationResult<T, U, V> validateGraph(Graph<T, U, V> graph, String nodeEnd) {
        ValidationResult<T, U, V> result = new ValidationResult<>();

        T end = graph.getNodeById(nodeEnd);
        if (end == null)
            return result;

        // Check duplicate property names
        ObjectMap<String, V> propertyNames = new ObjectMap<>();
        for (V property : graph.getProperties()) {
            String propertyName = property.getName();
            if (propertyNames.containsKey(propertyName)) {
                result.addErrorProperty(property);
                result.addErrorProperty(propertyNames.get(propertyName));
            }
            propertyNames.put(propertyName, property);
        }

        boolean cyclic = isCyclic(result, graph, nodeEnd);
        if (!cyclic) {
            // Do other Validation
            validateNode(result, graph, nodeEnd, new ObjectMap<String, NodeOutputs>());
        }
        return result;
    }

    private NodeOutputs validateNode(ValidationResult<T, U, V> result, Graph<T, U, V> graph, String nodeId,
                                     ObjectMap<String, NodeOutputs> nodeOutputs) {
        // Check if already validated
        NodeOutputs outputs = nodeOutputs.get(nodeId);
        if (outputs != null)
            return outputs;

        T thisNode = graph.getNodeById(nodeId);
        ObjectSet<String> validatedFields = new ObjectSet<>();
        ObjectMap<String, Array<String>> inputsTypes = new ObjectMap<>();
        for (String fieldTo : getInputFields(graph, nodeId)) {
            Array<String> inputType = new Array<>();
            for (U incomingConnection : getIncomingConnections(graph, nodeId, fieldTo)) {
                GraphNodeInput input = thisNode.getConfiguration().getNodeInputs().get(fieldTo);
                validatedFields.add(fieldTo);

                NodeOutputs outputFromRemoteNode = validateNode(result, graph, incomingConnection.getNodeFrom(), nodeOutputs);
                String outputType = outputFromRemoteNode.outputs.get(incomingConnection.getFieldFrom());
                if (!input.getAcceptedPropertyTypes().contains(outputType, false))
                    result.addErrorConnection(incomingConnection);
                inputType.add(outputType);
            }
            inputsTypes.put(fieldTo, inputType);
        }

        for (GraphNodeInput input : thisNode.getConfiguration().getNodeInputs().values()) {
            if (input.isRequired() && !validatedFields.contains(input.getFieldId())) {
                result.addErrorConnector(new NodeConnector(nodeId, input.getFieldId()));
            }
        }

        boolean valid = thisNode.getConfiguration().isValid(inputsTypes, graph.getProperties());
        if (!valid)
            result.addErrorNode(thisNode);

        ObjectMap<String, String> nodeOutputMap = new ObjectMap<>();
        for (GraphNodeOutput value : thisNode.getConfiguration().getNodeOutputs().values()) {
            String fieldType = value.determineFieldType(inputsTypes);
            nodeOutputMap.put(value.getFieldId(), fieldType);
        }

        NodeOutputs nodeOutput = new NodeOutputs(nodeOutputMap);
        nodeOutputs.put(nodeId, nodeOutput);
        return nodeOutput;
    }

    private Iterable<String> getInputFields(Graph<T, U, V> graph, String nodeId) {
        T nodeById = graph.getNodeById(nodeId);
        if (nodeById == null)
            throw new IllegalArgumentException("Node not found");
        NodeConfiguration configuration = nodeById.getConfiguration();
        if (configuration == null)
            System.out.println("??");
        return new ObjectMap.Keys<>(configuration.getNodeInputs());
    }

    private Iterable<U> getIncomingConnections(Graph<T, U, V> graph, String nodeId) {
        Array<U> result = new Array<>();
        for (U connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId))
                result.add(connection);
        }
        return result;
    }

    private Iterable<U> getIncomingConnections(Graph<T, U, V> graph, String nodeId, String fieldTo) {
        Array<U> result = new Array<>();
        for (U connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId) && connection.getFieldTo().equals(fieldTo))
                result.add(connection);
        }
        return result;
    }

    private boolean outputAcceptsPropertyType(GraphNodeOutput output, Array<String> acceptedPropertyTypes) {
        Array<String> producablePropertyTypes = output.getProducableFieldTypes();
        for (String acceptedFieldType : acceptedPropertyTypes) {
            if (producablePropertyTypes.contains(acceptedFieldType, false))
                return true;
        }
        return false;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private boolean isCyclicUtil(ValidationResult<T, U, V> validationResult, Graph<T, U, V> graph, String nodeId, ObjectSet<String> visited,
                                 ObjectSet<String> recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.contains(nodeId)) {
            validationResult.addErrorNode(graph.getNodeById(nodeId));
            return true;
        }

        if (visited.contains(nodeId))
            return false;

        visited.add(nodeId);
        recStack.add(nodeId);

        ObjectSet<String> connectedNodes = new ObjectSet<>();
        for (U incomingConnection : getIncomingConnections(graph, nodeId)) {
            connectedNodes.add(incomingConnection.getNodeFrom());
        }

        for (String connectedNode : connectedNodes) {
            if (isCyclicUtil(validationResult, graph, connectedNode, visited, recStack)) {
                return true;
            }
        }
        recStack.remove(nodeId);

        return false;
    }

    private boolean isCyclic(ValidationResult<T, U, V> validationResult, Graph<T, U, V> graph, String start) {
        ObjectSet<String> visited = new ObjectSet<>();
        ObjectSet<String> recStack = new ObjectSet<>();

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        if (isCyclicUtil(validationResult, graph, start, visited, recStack)) {
            return true;
        }

        for (T node : graph.getNodes()) {
            String nodeId = node.getId();
            if (!visited.contains(nodeId)) {
                validationResult.addWarningNode(node);
            }
        }
        return false;
    }

    private static class NodeOutputs {
        private final ObjectMap<String, String> outputs;

        public NodeOutputs(ObjectMap<String, String> outputs) {
            this.outputs = outputs;
        }
    }

    public static class ValidationResult<T extends GraphNode, U extends GraphConnection, V extends GraphProperty> {
        private final ObjectSet<T> errorNodes = new ObjectSet<>();
        private final ObjectSet<T> warningNodes = new ObjectSet<>();
        private final ObjectSet<U> errorConnections = new ObjectSet<>();
        private final ObjectSet<NodeConnector> errorConnectors = new ObjectSet<>();
        private final ObjectSet<V> errorProperties = new ObjectSet<>();

        public void addErrorNode(T node) {
            errorNodes.add(node);
        }

        public void addWarningNode(T node) {
            warningNodes.add(node);
        }

        public void addErrorConnection(U connection) {
            errorConnections.add(connection);
        }

        public void addErrorConnector(NodeConnector nodeConnector) {
            errorConnectors.add(nodeConnector);
        }

        public void addErrorProperty(V property) {
            errorProperties.add(property);
        }

        public ObjectSet<T> getErrorNodes() {
            return errorNodes;
        }

        public ObjectSet<T> getWarningNodes() {
            return warningNodes;
        }

        public ObjectSet<U> getErrorConnections() {
            return errorConnections;
        }

        public ObjectSet<NodeConnector> getErrorConnectors() {
            return errorConnectors;
        }

        public ObjectSet<V> getErrorProperties() {
            return errorProperties;
        }

        public boolean hasErrors() {
            return !errorNodes.isEmpty() || !errorConnections.isEmpty() || !errorConnectors.isEmpty() || !errorProperties.isEmpty();
        }

        public boolean hasWarnings() {
            return !warningNodes.isEmpty();
        }
    }
}
