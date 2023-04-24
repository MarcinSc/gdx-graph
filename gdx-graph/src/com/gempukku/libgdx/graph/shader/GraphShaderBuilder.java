package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;

public class GraphShaderBuilder {
    public enum ShaderType {
        Model, Particles, Screen, Depth
    }

    public static GraphShader buildShader(GraphWithProperties graph) {
        GraphShader graphShader = new GraphShader("", null);
        ShaderType shaderType = getShaderType(graph.getType());
        buildShader(graph, shaderType, false, graphShader);
        return graphShader;
    }

    private static ShaderType getShaderType(String shaderType) {
        switch (shaderType) {
            case ModelShaderGraphType.TYPE:
                return ShaderType.Model;
            case ParticleEffectGraphType.TYPE:
                return ShaderType.Particles;
            case ScreenShaderGraphType.TYPE:
                return ShaderType.Screen;
            case ShadowShaderGraphType.TYPE:
                return ShaderType.Depth;
        }
        return null;
    }

    private static GraphConfiguration[] getGraphConfigurations(ShaderType shaderType) {
        switch (shaderType) {
            case Model:
                return ((ShaderGraphType) GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE)).getConfigurations();
            case Particles:
                return ((ShaderGraphType) GraphTypeRegistry.findGraphType(ParticleEffectGraphType.TYPE)).getConfigurations();
            case Screen:
                return ((ShaderGraphType) GraphTypeRegistry.findGraphType(ScreenShaderGraphType.TYPE)).getConfigurations();
            case Depth:
                return ((ShaderGraphType) GraphTypeRegistry.findGraphType(ShadowShaderGraphType.TYPE)).getConfigurations();
        }
        return null;
    }

    private static void buildShader(GraphWithProperties graph, ShaderType shaderType, boolean designTime, GraphShader graphShader) {
        GraphConfiguration[] configurations = getGraphConfigurations(shaderType);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);

        switch (shaderType) {
            case Screen:
                buildScreenVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
                break;
            default:
                buildVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        }
        switch (shaderType) {
            case Model:
                buildModelFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
                break;
            case Particles:
                buildParticlesFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
                break;
            case Screen:
                buildScreenFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
                graphShader.setCulling(BasicShader.Culling.back);
                graphShader.setDepthTesting(BasicShader.DepthTesting.disabled);
                graphShader.setDepthWriting(false);
                break;
            case Depth:
                buildDepthFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
                graphShader.setDepthWriting(true);
                break;
        }

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();
        switch (shaderType) {
            case Model:
                debugShaders("color", vertexShader, fragmentShader);
                break;
            case Particles:
                debugShaders("particles", vertexShader, fragmentShader);
                break;
            case Screen:
                debugShaders("screen", vertexShader, fragmentShader);
                break;
            case Depth:
                debugShaders("depth", vertexShader, fragmentShader);
                break;
        }

        graphShader.setProgram(vertexShader, fragmentShader);
    }

    public static GraphShader buildModelShader(String tag, Texture defaultTexture,
                                               GraphWithProperties graph,
                                               boolean designTime) {
        GraphShader graphShader = new GraphShader(tag, defaultTexture);

        buildShader(graph, ShaderType.Model, designTime, graphShader);
        graphShader.init();

        return graphShader;
    }

    public static GraphShader buildParticlesShader(String tag, Texture defaultTexture, GraphWithProperties graph,
                                                   boolean designTime) {
        GraphShader graphShader = new GraphShader(tag, defaultTexture);

        buildShader(graph, ShaderType.Particles, designTime, graphShader);
        graphShader.init();

        return graphShader;
    }

    public static GraphShader buildScreenShader(String tag, Texture defaultTexture,
                                                      GraphWithProperties graph,
                                                      boolean designTime) {
        GraphShader graphShader = new GraphShader(tag, defaultTexture);

        buildShader(graph, ShaderType.Screen, designTime, graphShader);
        graphShader.init();

        return graphShader;
    }

    public static GraphShader buildModelDepthShader(String tag, Texture defaultTexture,
                                                    GraphWithProperties graph,
                                                    boolean designTime) {
        GraphShader graphShader = new GraphShader(tag, defaultTexture);

        buildShader(graph, ShaderType.Depth, designTime, graphShader);
        graphShader.init();

        return graphShader;
    }

    private static void initialize(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
                                   GraphConfiguration... graphConfigurations) {
        initializePropertyMap(graphShader, graph, designTime, graphConfigurations);
        initializeShaders(vertexShaderBuilder, fragmentShaderBuilder);
        setupOpenGLSettings(graphShader, graph);
    }

    private static void initializePropertyMap(GraphShader graphShader, GraphWithProperties graph, boolean designTime,
                                              GraphConfiguration... graphConfigurations) {
        int index = 0;
        for (GraphProperty property : graph.getProperties()) {
            String name = property.getName();
            graphShader.addPropertySource(name, findPropertyProducerByType(property.getType(), graphConfigurations).createProperty(index++, name, property.getData(), property.getLocation(), designTime));
        }
    }

    private static void setupOpenGLSettings(GraphShader graphShader, GraphWithProperties graph) {
        GraphNode endNode = graph.getNodeById("end");
        JsonValue data = endNode.getData();

        String cullingValue = data.getString("culling", null);
        if (cullingValue != null)
            graphShader.setCulling(BasicShader.Culling.valueOf(cullingValue));

        String depthTest = data.getString("depthTest", null);
        if (depthTest != null)
            graphShader.setDepthTesting(BasicShader.DepthTesting.valueOf(depthTest.replace(' ', '_')));

        boolean depthWrite = data.getBoolean("depthWrite", false);
        graphShader.setDepthWriting(depthWrite);

        boolean blending = data.getBoolean("blending", false);
        graphShader.setBlending(blending);

        String blendingSourceFactor = data.getString("blendingSourceFactor", null);
        if (blendingSourceFactor != null)
            graphShader.setBlendingSourceFactor(BasicShader.BlendingFactor.valueOf(blendingSourceFactor.replace(' ', '_')));

        String blendingDestinationFactor = data.getString("blendingDestinationFactor", null);
        if (blendingDestinationFactor != null)
            graphShader.setBlendingDestinationFactor(BasicShader.BlendingFactor.valueOf(blendingDestinationFactor.replace(' ', '_')));
    }

    private static void debugShaders(String type, String vertexShader, String fragmentShader) {
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "Vertex " + type + " shader:");
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "\n" + vertexShader);
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "Fragment " + type + " shader:");
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "\n" + fragmentShader);
    }

    private static void buildDepthFragmentShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        fragmentShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping,
                "Near/far clipping");
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");
        fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");

        if (!fragmentShaderBuilder.containsFunction("packFloatToVec3")) {
            fragmentShaderBuilder.addFunction("packFloatToVec3", GLSLFragmentReader.getFragment("packFloatToVec3"));
        }

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);
        fragmentShaderBuilder.addMainLine("gl_FragColor = vec4(packFloatToVec3(distance(v_position_world, u_cameraPosition), u_cameraClipping.x, u_cameraClipping.y), 1.0);");
    }

    private static void buildParticlesFragmentShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        // Fragment part
        if (!vertexShaderBuilder.hasVaryingVariable("v_deathTime")) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(2048, 1, "a_deathTime"), "float", "Particle death-time");
            vertexShaderBuilder.addVaryingVariable("v_deathTime", "float");
            vertexShaderBuilder.addMainLine("v_deathTime = a_deathTime;");

            fragmentShaderBuilder.addVaryingVariable("v_deathTime", "float");
        }

        fragmentShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");
        fragmentShaderBuilder.addMainLine("if (u_time >= v_deathTime)");
        fragmentShaderBuilder.addMainLine("  discard;");

        buildFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
    }

    private static void buildScreenFragmentShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        buildFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
    }

    private static void buildModelFragmentShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        buildFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, configurations);
    }

    private static void buildFragmentShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        // Fragment part
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertices(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);
        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildVertexShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, configurations);

        String positionType = graph.getNodeById("end").getData().getString("positionType", "World space");
        if (positionType.equals("World space")) {
            vertexShaderBuilder.addMainLine("vec3 positionWorld = " + positionField.getRepresentation() + ";");
        } else if (positionType.equals("Object space")) {
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans,
                    "Model to world transformation");
            vertexShaderBuilder.addMainLine("vec3 positionWorld = (u_worldTrans * vec4(" + positionField.getRepresentation() + ", 1.0)).xyz;");
        }

        vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
        vertexShaderBuilder.addMainLine("v_position_world = positionWorld;");

        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans,
                "Project view transformation");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * vec4(positionWorld, 1.0);");
    }

    private static void applyAlphaDiscard(FragmentShaderBuilder fragmentShaderBuilder, GraphShaderNodeBuilder.FieldOutput alphaField, String alpha, GraphShaderNodeBuilder.FieldOutput alphaClipField, String alphaClip) {
        if (alphaField != null || alphaClipField != null) {
            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }

    private static void buildScreenVertexShader(GraphWithProperties graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration[] configurations) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), "vec3", "Position");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = vec4((a_position.xy * 2.0 - 1.0), 1.0, 1.0);");
    }

    private static GraphShaderNodeBuilder.FieldOutput getOutput(Array<GraphConnection> connections,
                                                                boolean designTime, boolean fragmentShader,
                                                                GraphWithProperties graph,
                                                                GraphShaderContext context, GraphShader graphShader, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
                                                                VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration... graphConfigurations) {
        if (connections == null || connections.size == 0)
            return null;
        GraphConnection connection = connections.get(0);
        ObjectMap<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, connection.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        return output.get(connection.getFieldFrom());
    }


    private static void initializeShaders(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        vertexShaderBuilder.addInitialLine("#ifdef GL_ES");
        vertexShaderBuilder.addInitialLine("#define LOWP lowp");
        vertexShaderBuilder.addInitialLine("#define MED mediump");
        vertexShaderBuilder.addInitialLine("#define HIGH highp");
        vertexShaderBuilder.addInitialLine("precision mediump float;");
        vertexShaderBuilder.addInitialLine("#else");
        vertexShaderBuilder.addInitialLine("#define MED");
        vertexShaderBuilder.addInitialLine("#define LOWP");
        vertexShaderBuilder.addInitialLine("#define HIGH");
        vertexShaderBuilder.addInitialLine("#endif");

        fragmentShaderBuilder.addInitialLine("#ifdef GL_ES");
        fragmentShaderBuilder.addInitialLine("#define LOWP lowp");
        fragmentShaderBuilder.addInitialLine("#define MED mediump");
        fragmentShaderBuilder.addInitialLine("#define HIGH highp");
        fragmentShaderBuilder.addInitialLine("precision mediump float;");
        fragmentShaderBuilder.addInitialLine("#else");
        fragmentShaderBuilder.addInitialLine("#define MED");
        fragmentShaderBuilder.addInitialLine("#define LOWP");
        fragmentShaderBuilder.addInitialLine("#define HIGH");
        fragmentShaderBuilder.addInitialLine("#endif");
    }

    private static ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> buildNode(
            boolean designTime, boolean fragmentShader,
            GraphWithProperties graph,
            GraphShaderContext context, GraphShader graphShader, String nodeId, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration... graphConfigurations) {
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
                Array<GraphConnection> vertexInfos = findInputVertices(graph, nodeId, fieldId);
                if (vertexInfos.size == 0 && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                Array<String> fieldTypes = new Array<>();
                Array<GraphShaderNodeBuilder.FieldOutput> fieldOutputs = new Array<>();
                for (GraphConnection vertexInfo : vertexInfos) {
                    ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    fieldTypes.add(fieldType.getName());
                    fieldOutputs.add(fieldOutput);
                }
                if (!acceptsInputTypes(nodeInput.getAcceptedPropertyTypes(), fieldTypes))
                    throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                inputFields.put(fieldId, fieldOutputs);
            }
            ObjectSet<String> requiredOutputs = findRequiredOutputs(graph, nodeId);
            if (fragmentShader) {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildFragmentNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context, graphShader);
            } else {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildVertexNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, context, graphShader);
            }
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private static boolean acceptsInputTypes(Array<String> acceptedPropertyTypes, Array<String> fieldTypes) {
        for (String fieldType : fieldTypes) {
            if (!acceptedPropertyTypes.contains(fieldType, false))
                return false;
        }
        return true;
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

    private static Array<GraphConnection> findInputVertices(GraphWithProperties graph,
                                                            String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }

    private static GraphShaderPropertyProducer findPropertyProducerByType(String type, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            for (GraphShaderPropertyProducer graphShaderPropertyProducer : configuration.getPropertyProducers()) {
                if (graphShaderPropertyProducer.getType().getName().equals(type))
                    return graphShaderPropertyProducer;
            }
        }

        return null;
    }
}
