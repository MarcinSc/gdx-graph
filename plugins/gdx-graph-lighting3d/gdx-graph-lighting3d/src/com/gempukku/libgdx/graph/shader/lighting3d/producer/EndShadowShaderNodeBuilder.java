package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.lighting3d.config.EndShadowShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;

public class EndShadowShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public EndShadowShaderNodeBuilder() {
        super(new EndShadowShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(
            boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        // Dummy node
        return null;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(
            boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        // Dummy node
        return null;
    }
}
