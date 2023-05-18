package com.gempukku.libgdx.graph.shader.builder.recipe;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;

public class DefaultGraphShaderRecipe implements GraphShaderRecipe {
    private final Array<GraphShaderRecipeIngredient> initIngredients = new Array<>();
    private final Array<GraphShaderRecipeIngredient> vertexShaderIngredients = new Array<>();
    private final Array<GraphShaderRecipeIngredient> fragmentShaderIngredients = new Array<>();
    private final Array<GraphShaderRecipeIngredient> finalizeIngredients = new Array<>();

    @Override
    public GraphShader buildGraphShader(String tag, boolean designTime, GraphWithProperties graph, FileHandleResolver assetResolver) {
        GraphShader graphShader = new GraphShader(tag);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        for (GraphShaderRecipeIngredient initIngredient : initIngredients) {
            initIngredient.processIngredient(designTime, graph, graphShader, vertexShaderBuilder, fragmentShaderBuilder, null, assetResolver);
        }

        OutputResolver vertexOutputResolver = new OutputResolver(
                designTime, graph, assetResolver, graphShader, false, vertexShaderBuilder, fragmentShaderBuilder);
        for (GraphShaderRecipeIngredient vertexShaderIngredient : vertexShaderIngredients) {
            vertexShaderIngredient.processIngredient(designTime, graph, graphShader, vertexShaderBuilder, fragmentShaderBuilder, vertexOutputResolver, assetResolver);
        }

        OutputResolver fragmentOutputResolver = new OutputResolver(
                designTime, graph, assetResolver, graphShader, true, vertexShaderBuilder, fragmentShaderBuilder);

        for (GraphShaderRecipeIngredient fragmentShaderIngredient : fragmentShaderIngredients) {
            fragmentShaderIngredient.processIngredient(designTime, graph, graphShader, vertexShaderBuilder, fragmentShaderBuilder, fragmentOutputResolver, assetResolver);
        }

        for (GraphShaderRecipeIngredient finalizeIngredient : finalizeIngredients) {
            finalizeIngredient.processIngredient(designTime, graph, graphShader, vertexShaderBuilder, fragmentShaderBuilder, null, assetResolver);
        }

        return graphShader;
    }

    public void addInitIngredient(GraphShaderRecipeIngredient ingredient) {
        initIngredients.add(ingredient);
    }

    public void addVertexShaderIngredient(GraphShaderRecipeIngredient ingredient) {
        vertexShaderIngredients.add(ingredient);
    }

    public void addFragmentShaderIngredient(GraphShaderRecipeIngredient ingredient) {
        fragmentShaderIngredients.add(ingredient);
    }

    public void addFinalizeShaderIngredient(GraphShaderRecipeIngredient ingredient) {
        finalizeIngredients.add(ingredient);
    }

    private static class OutputResolver implements GraphShaderRecipeIngredient.GraphShaderOutputResolver {
        private final boolean designTime;
        private final GraphWithProperties graph;
        private final FileHandleResolver assetResolver;
        private final GraphShader graphShader;
        private final boolean fragmentShader;
        private final VertexShaderBuilder vertexShaderBuilder;
        private final FragmentShaderBuilder fragmentShaderBuilder;
        private final ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs;
        private final GraphConfiguration[] graphConfigurations;

        public OutputResolver(
                boolean designTime, GraphWithProperties graph, FileHandleResolver assetResolver,
                GraphShader graphShader, boolean fragmentShader,
                VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
            this.designTime = designTime;
            this.graph = graph;
            this.assetResolver = assetResolver;
            this.graphShader = graphShader;
            this.fragmentShader = fragmentShader;
            this.vertexShaderBuilder = vertexShaderBuilder;
            this.fragmentShaderBuilder = fragmentShaderBuilder;
            this.nodeOutputs = new ObjectMap<>();
            this.graphConfigurations = GraphTypeRegistry.findGraphType(graph.getType(), ShaderGraphType.class).getConfigurations();
        }

        @Override
        public GraphShaderNodeBuilder.FieldOutput getSingleOutput(String nodeId, String property) {
            ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> nodeOutputs = buildNode(designTime, fragmentShader, graph, graphShader, graphShader, assetResolver,
                    nodeId, this.nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
            return nodeOutputs.get(property);
        }

        @Override
        public GraphShaderNodeBuilder.FieldOutput getSingleOutputForInput(String inputNodeId, String inputProperty) {
            Array<GraphConnection> inputConnections = findInputConnectionsTo(graph, inputNodeId, inputProperty);
            if (inputConnections.size>1)
                throw new IllegalArgumentException("More than one inputs found: "+inputConnections.size);
            if (inputConnections.size == 0)
                return null;
            GraphConnection inputConnection = inputConnections.get(0);
            return getSingleOutput(inputConnection.getNodeFrom(), inputConnection.getFieldFrom());
        }
    }

    private static ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> buildNode(
            boolean designTime, boolean fragmentShader,
            GraphWithProperties graph,
            GraphShaderContext context, GraphShader graphShader, FileHandleResolver assetResolver,
            String nodeId, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphConfiguration... graphConfigurations) {
        ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> nodeOutput = nodeOutputs.get(nodeId);
        if (nodeOutput == null) {
            GraphNode nodeInfo = graph.getNodeById(nodeId);
            String nodeInfoType = nodeInfo.getType();
            GraphShaderNodeBuilder nodeBuilder = getNodeBuilder(nodeInfoType, graphConfigurations);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfoType);
            ObjectMap<String, Array<GraphShaderNodeBuilder.FieldOutput>> inputFields = new ObjectMap<>();
            for (GraphNodeInput nodeInput : new ObjectMap.Values<>(nodeBuilder.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
                String fieldId = nodeInput.getFieldId();
                Array<GraphConnection> vertexInfos = findInputConnectionsTo(graph, nodeId, fieldId);
                if (vertexInfos.size == 0 && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                Array<String> fieldTypes = new Array<>();
                Array<GraphShaderNodeBuilder.FieldOutput> fieldOutputs = new Array<>();
                for (GraphConnection vertexInfo : vertexInfos) {
                    ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, assetResolver,vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    fieldTypes.add(fieldType.getName());
                    fieldOutputs.add(fieldOutput);
                }
                inputFields.put(fieldId, fieldOutputs);
            }
            ObjectSet<String> requiredOutputs = findRequiredOutputs(graph, nodeId);
            if (fragmentShader) {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildFragmentNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context, graphShader, assetResolver);
            } else {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildVertexNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, context, graphShader, assetResolver);
            }
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private static GraphShaderNodeBuilder getNodeBuilder(String nodeInfoType, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            GraphShaderNodeBuilder graphShaderNodeBuilder = configuration.getGraphShaderNodeBuilder(nodeInfoType);
            if (graphShaderNodeBuilder != null)
                return graphShaderNodeBuilder;
        }

        return null;
    }

    private static ObjectSet<String> findRequiredOutputs(GraphWithProperties graph,
                                                         String nodeId) {
        ObjectSet<String> result = new ObjectSet<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId))
                result.add(vertex.getFieldFrom());
        }
        return result;
    }

    private static Array<GraphConnection> findInputConnectionsTo(GraphWithProperties graph,
                                                            String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }
}
