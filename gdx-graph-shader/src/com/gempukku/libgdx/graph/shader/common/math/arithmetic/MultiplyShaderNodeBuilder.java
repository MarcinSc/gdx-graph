package com.gempukku.libgdx.graph.shader.common.math.arithmetic;

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
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.MultiplyShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class MultiplyShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public MultiplyShaderNodeBuilder() {
        super(new MultiplyShaderNodeConfiguration());
    }


    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return null;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return null;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildAddCommonNode(designTime, nodeId, data, inputs, producedOutputs, vertexShaderBuilder, graphShader);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildAddCommonNode(designTime, nodeId, data, inputs, producedOutputs, fragmentShaderBuilder, graphShader);
    }

    private ObjectMap<String, ? extends FieldOutput> buildAddCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputArrays, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader) {
        Array<FieldOutput> inputs = inputArrays.get("inputs");
        ShaderFieldType resultType = determineOutputType(inputs);

        commonShaderBuilder.addMainLine("// Multiply node");
        String name = "result_" + nodeId;
        StringBuilder multiplyText = new StringBuilder();
        for (FieldOutput input : inputs) {
            multiplyText.append(" * ");
            multiplyText.append(input.getRepresentation());
        }
        multiplyText.replace(0, 3, "");

        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + multiplyText + ";");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
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
}
