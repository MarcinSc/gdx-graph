package com.gempukku.libgdx.graph.shader.common.math.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.math.common.MinimumShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public class MinimumShaderNodeBuilder implements GraphShaderNodeBuilder {
    private NodeConfiguration nodeConfiguration;

    public MinimumShaderNodeBuilder() {
        nodeConfiguration = new MinimumShaderNodeConfiguration();
    }

    @Override
    public String getType() {
        return nodeConfiguration.getType();
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        return nodeConfiguration;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, vertexShaderBuilder, graphShader, configuration);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, fragmentShaderBuilder, graphShader, configuration);
    }

    private ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        Array<FieldOutput> values = inputs.get("input");
        ShaderFieldType resultType = determineOutputType(values);

        commonShaderBuilder.addMainLine("// Minimum node");
        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = "+buildMinString(values)+";");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(resultType, name));
    }

    private static String buildMinString(Array<FieldOutput> values) {
        StringBuilder start = new StringBuilder();
        StringBuilder end = new StringBuilder();
        for (int i=0; i<values.size; i++) {
            if (i>0) {
                start.append(", ");
            }
            if (i<values.size-1) {
                start.append("min(");
            }
            start.append(values.get(i).getRepresentation());
            if (i<values.size-1) {
                end.append(")");
            }
        }
        return start.toString()+end.toString();
    }

    private ShaderFieldType determineOutputType(Array<FieldOutput> inputs) {
        ShaderFieldType result = ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Float);
        for (FieldOutput input : inputs) {
            ShaderFieldType fieldType = input.getFieldType();
            if (fieldType != result && (!result.getName().equals(ShaderFieldType.Float) && !fieldType.getName().equals(ShaderFieldType.Float)))
                throw new IllegalStateException("Invalid mix of input field types");
            if (!fieldType.getName().equals(ShaderFieldType.Float))
                result = fieldType;
        }
        return result;
    }

    public static void main(String[] str) {
        System.out.println(buildMinString(
                Array.<FieldOutput>with(
                        new DefaultFieldOutput("a", "a")
                )));
        System.out.println(buildMinString(
                Array.<FieldOutput>with(
                        new DefaultFieldOutput("a", "a"),
                        new DefaultFieldOutput("b", "b")
                )));
        System.out.println(buildMinString(
                Array.<FieldOutput>with(
                        new DefaultFieldOutput("a", "a"),
                        new DefaultFieldOutput("a", "b"),
                        new DefaultFieldOutput("b", "c")
                )));
        System.out.println(buildMinString(
                Array.<FieldOutput>with(
                        new DefaultFieldOutput("a", "a"),
                        new DefaultFieldOutput("a", "b"),
                        new DefaultFieldOutput("a", "c"),
                        new DefaultFieldOutput("b", "d")
                )));
    }
}
