package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public abstract class ConfigurationCommonShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ConfigurationCommonShaderNodeBuilder(NodeConfiguration configuration) {
        super(configuration);
    }

    protected abstract ObjectMap<String, ? extends FieldOutput> buildCommonNode(
            boolean designTime, String nodeId, JsonValue data,
            ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration);

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(
            boolean designTime, String nodeId, JsonValue data,
            ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder,
            GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, vertexShaderBuilder, graphShader, configuration);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(
            boolean designTime, String nodeId, JsonValue data,
            ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShader graphShader, PipelineRendererConfiguration configuration) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, fragmentShaderBuilder, graphShader, configuration);
    }
}
