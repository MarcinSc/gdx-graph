package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PreparedRenderingPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

import java.util.LinkedList;

public class PreparedRenderingPipelineImpl implements PreparedRenderingPipeline {
    private GraphWithProperties graph;
    private PipelineRequirements pipelineRequirements = new PipelineRequirements();

    private Iterable<PipelineNode> executionNodes;
    private String endNodeId;
    private EndPipelineNode endPipelineNode;

    private ObjectMap<PipelineNode, PipelineNode.PipelineRequirementsCallback> requirementsCallbacks;

    private final Function<GraphNode, NodeConfiguration> nodeConfigurationResolver;

    public PreparedRenderingPipelineImpl(GraphWithProperties graph,
                                         String endNodeId) {
        this.graph = graph;
        this.endNodeId = endNodeId;

        nodeConfigurationResolver = new Function<GraphNode, NodeConfiguration>() {
            @Override
            public NodeConfiguration evaluate(GraphNode value) {
                return RendererPipelineConfiguration.findProducer(value.getType()).getConfiguration(value.getData());
            }
        };
    }

    @Override
    public void initialize(PipelineDataProvider pipelineDataProvider) {
        ObjectMap<String, ObjectMap<String, String>> nodeOutputTypes = new ObjectMap<>();
        populateNodeOutputTypes(endNodeId, nodeOutputTypes);

        ObjectMap<String, PipelineNode> pipelineNodeMap = new ObjectMap<>();
        populatePipelineNodes(endNodeId, pipelineNodeMap, nodeOutputTypes);

        endPipelineNode = (EndPipelineNode) pipelineNodeMap.get(endNodeId);

        for (PipelineNode value : pipelineNodeMap.values()) {
            value.initializePipeline(pipelineDataProvider);
        }

        // Populate callbacks for "main" nodes
        ObjectMap<PipelineNode, PipelineNode.PipelineRequirementsCallback> requirementsCallbacks = new ObjectMap<>();
        populateCallbacks(pipelineNodeMap, requirementsCallbacks, endNodeId);
        this.requirementsCallbacks = requirementsCallbacks;

        // Order nodes by execution
        LinkedList<PipelineNode> nodesInExecutionOrder = new LinkedList<>();
        populateExecutionOrder(pipelineNodeMap, nodesInExecutionOrder, endNodeId);
        this.executionNodes = nodesInExecutionOrder;

        // Gather outputs
        ObjectMap<String, ObjectMap<String, PipelineNode.FieldOutput<?>>> outputs = gatherOutputs(pipelineNodeMap);

        // Setup inputs
        setupInputs(pipelineNodeMap, outputs);
    }

    private void populateExecutionOrder(ObjectMap<String, PipelineNode> pipelineNodeMap, LinkedList<PipelineNode> nodesInExecutionOrder, String nodeId) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        nodesInExecutionOrder.removeFirstOccurrence(pipelineNode);
        nodesInExecutionOrder.addFirst(pipelineNode);

        GraphConnection mainInputConnection = getMainInputConnection(nodeId);
        if (mainInputConnection != null) {
            populateExecutionOrder(pipelineNodeMap, nodesInExecutionOrder, mainInputConnection.getNodeFrom());
        }

        for (GraphConnection nonMainInputConnection : getNonMainInputConnections(nodeId)) {
            populateExecutionOrder(pipelineNodeMap, nodesInExecutionOrder, nonMainInputConnection.getNodeFrom());
        }
    }

    private void populateCallbacks(ObjectMap<String, PipelineNode> pipelineNodeMap, ObjectMap<PipelineNode, PipelineNode.PipelineRequirementsCallback> requirementsCallbacks, String nodeId) {
        GraphConnection mainInputConnection = getMainInputConnection(nodeId);
        if (mainInputConnection != null) {
            Array<PipelineNode> mainNodeLine = new Array<>();
            mainNodeLine.insert(0, pipelineNodeMap.get(nodeId));
            while (mainInputConnection != null) {
                String nodeFrom = mainInputConnection.getNodeFrom();
                mainNodeLine.insert(0, pipelineNodeMap.get(nodeFrom));
                mainInputConnection = getMainInputConnection(nodeFrom);
            }

            for (int i = 0; i < mainNodeLine.size; i++) {
                requirementsCallbacks.put(mainNodeLine.get(i),
                        new NodesPipelineRequirementsCallback(mainNodeLine, i));
            }
        }

        Iterable<GraphConnection> nonMainInputConnections = getNonMainInputConnections(nodeId);
        for (GraphConnection nonMainInputConnection : nonMainInputConnections) {
            populateCallbacks(pipelineNodeMap, requirementsCallbacks, nonMainInputConnection.getNodeFrom());
        }
    }

    private ObjectMap<String, ObjectMap<String, PipelineNode.FieldOutput<?>>> gatherOutputs(ObjectMap<String, PipelineNode> pipelineNodeMap) {
        ObjectMap<String, ObjectMap<String, PipelineNode.FieldOutput<?>>> outputs = new ObjectMap<>();
        for (ObjectMap.Entry<String, PipelineNode> node : pipelineNodeMap.entries()) {
            ObjectMap<String, PipelineNode.FieldOutput<?>> nodeOutputs = node.value.getOutputs();
            outputs.put(node.key, nodeOutputs);
        }
        return outputs;
    }

    private void setupInputs(ObjectMap<String, PipelineNode> pipelineNodeMap, ObjectMap<String, ObjectMap<String, PipelineNode.FieldOutput<?>>> outputs) {
        for (ObjectMap.Entry<String, PipelineNode> node : pipelineNodeMap.entries()) {
            String nodeId = node.key;
            GraphNode graphNode = graph.getNodeById(nodeId);
            ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputs = new ObjectMap<>();
            for (String key : nodeConfigurationResolver.evaluate(graphNode).getNodeInputs().keys()) {
                Array<GraphConnection> inputConnections = findInputConnections(nodeId, key);
                if (inputConnections.size > 0) {
                    Array<PipelineNode.FieldOutput<?>> inputValues = new Array<>();
                    for (GraphConnection inputConnection : inputConnections) {
                        inputValues.add(outputs.get(inputConnection.getNodeFrom()).get(inputConnection.getFieldFrom()));
                    }

                    inputs.put(key, inputValues);
                }
            }

            node.value.setInputs(inputs);
        }
    }

    @Override
    public void startFrame() {
        for (PipelineNode value : executionNodes) {
            value.startFrame();
        }
    }

    @Override
    public RenderPipeline execute(PipelineRenderingContext pipelineRenderingContext) {
        for (PipelineNode value : executionNodes) {
            value.executeNode(pipelineRenderingContext, requirementsCallbacks.get(value));
        }
        return endPipelineNode.getRenderPipeline();
    }

    @Override
    public void endFrame() {
        for (PipelineNode value : executionNodes) {
            value.endFrame();
        }
    }

    @Override
    public void dispose() {
        for (PipelineNode value : executionNodes) {
            value.dispose();
        }
    }

    private void populateNodeOutputTypes(String nodeId, ObjectMap<String, ObjectMap<String, String>> nodeOutputTypes) {
        if (nodeOutputTypes.containsKey(nodeId))
            return;

        GraphNode nodeInfo = graph.getNodeById(nodeId);
        String nodeInfoType = nodeInfo.getType();
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.findProducer(nodeInfoType);
        if (nodeProducer == null)
            throw new IllegalStateException("Unable to find node producer for type: " + nodeInfoType);
        ObjectMap<String, Array<String>> inputTypes = new ObjectMap<>();
        for (GraphNodeInput nodeInput : new ObjectMap.Values<>(nodeProducer.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
            String inputName = nodeInput.getFieldId();
            Array<GraphConnection> graphConnections = findInputConnections(nodeId, inputName);
            if (graphConnections.size == 0 && nodeInput.isRequired())
                throw new IllegalStateException("Required input not provided");

            Array<String> fieldTypes = new Array<>();
            for (GraphConnection graphConnection : graphConnections) {
                populateNodeOutputTypes(graphConnection.getNodeFrom(), nodeOutputTypes);
                ObjectMap<String, String> outputTypes = nodeOutputTypes.get(graphConnection.getNodeFrom());
                String fieldType = outputTypes.get(graphConnection.getFieldFrom());
                fieldTypes.add(fieldType);
            }
            if (!acceptsInputTypes(nodeInput.getAcceptedPropertyTypes(), fieldTypes))
                throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
            inputTypes.put(inputName, fieldTypes);
        }

        ObjectMap<String, String> outputTypes = nodeProducer.getOutputTypes(nodeInfo.getData(), inputTypes);

        for (String outputField : outputTypes.keys()) {
            if (!isConnectionUsed(nodeId, outputField))
                outputTypes.remove(outputField);
        }

        nodeOutputTypes.put(nodeId, outputTypes);
    }

    private boolean acceptsInputTypes(Array<String> acceptedPropertyTypes, Array<String> fieldTypes) {
        for (String fieldType : fieldTypes) {
            if (!acceptedPropertyTypes.contains(fieldType, false))
                return false;
        }
        return true;
    }

    private boolean isConnectionUsed(String nodeId, String outputField) {
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId) && vertex.getFieldFrom().equals(outputField))
                return true;
        }
        return false;
    }

    private void populatePipelineNodes(String nodeId, ObjectMap<String, PipelineNode> pipelineNodeMap,
                                       ObjectMap<String, ObjectMap<String, String>> pipelineNodeOutputTypes) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        if (pipelineNode != null)
            return;

        GraphNode nodeInfo = graph.getNodeById(nodeId);
        String nodeInfoType = nodeInfo.getType();
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.findProducer(nodeInfoType);
        if (nodeProducer == null)
            throw new IllegalStateException("Unable to find node producer for type: " + nodeInfoType);
        ObjectMap<String, Array<String>> inputTypes = new ObjectMap<>();
        for (GraphNodeInput nodeInput : new ObjectMap.Values<>(nodeProducer.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
            String inputName = nodeInput.getFieldId();
            Array<GraphConnection> graphConnections = findInputConnections(nodeId, inputName);
            if (graphConnections.size == 0 && nodeInput.isRequired())
                throw new IllegalStateException("Required input not provided");

            Array<String> fieldTypes = new Array<>();
            for (GraphConnection graphConnection : graphConnections) {
                populatePipelineNodes(graphConnection.getNodeFrom(), pipelineNodeMap, pipelineNodeOutputTypes);
                ObjectMap<String, String> outputTypes = pipelineNodeOutputTypes.get(graphConnection.getNodeFrom());
                String fieldType = outputTypes.get(graphConnection.getFieldFrom());
                fieldTypes.add(fieldType);
            }
            if (!acceptsInputTypes(nodeInput.getAcceptedPropertyTypes(), fieldTypes))
                throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
            inputTypes.put(inputName, fieldTypes);
        }
        pipelineNode = nodeProducer.createNode(nodeInfo.getData(), inputTypes, pipelineNodeOutputTypes.get(nodeId));
        pipelineNodeMap.put(nodeId, pipelineNode);
    }

    private GraphConnection getMainInputConnection(String nodeId) {
        for (GraphNodeInput value : nodeConfigurationResolver.evaluate(graph.getNodeById(nodeId)).getNodeInputs().values()) {
            if (value.isMainConnection())
                return findInputConnections(nodeId, value.getFieldId()).get(0);
        }
        return null;
    }

    private Iterable<GraphConnection> getNonMainInputConnections(String nodeId) {
        ObjectMap<String, GraphNodeInput> inputs = nodeConfigurationResolver.evaluate(graph.getNodeById(nodeId)).getNodeInputs();
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && !inputs.get(vertex.getFieldTo()).isMainConnection())
                result.add(vertex);
        }
        return result;
    }

    private Array<GraphConnection> findInputConnections(String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }

    private class NodesPipelineRequirementsCallback implements PipelineNode.PipelineRequirementsCallback {
        private Array<PipelineNode> mainNodeLine;
        private int nodeIndex;

        public NodesPipelineRequirementsCallback(Array<PipelineNode> mainNodeLine, int nodeIndex) {
            this.mainNodeLine = mainNodeLine;
            this.nodeIndex = nodeIndex;
        }

        @Override
        public PipelineRequirements getPipelineRequirements() {
            pipelineRequirements.reset();
            for (int i = nodeIndex + 1; i < mainNodeLine.size; i++) {
                mainNodeLine.get(i).processPipelineRequirements(pipelineRequirements);
            }
            return pipelineRequirements;
        }
    }
}
